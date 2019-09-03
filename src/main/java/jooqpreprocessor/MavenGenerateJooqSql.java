package jooqpreprocessor;

import jooqpreprocessor.parsers.*;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_RESOURCES;

@Mojo( defaultPhase = GENERATE_RESOURCES, name = "generate" )
public final class MavenGenerateJooqSql extends LogSuppressingMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    public MavenProject project;

    @Parameter(defaultValue = "true")
    public boolean enabled;

    @Parameter(defaultValue = "src/main/resources/db/migration")
    public String migrationSqlDir;

    @Parameter(defaultValue = "target/generated-resources/db/fulldb.sql")
    public String generationSqlFile;

    public void execute() throws MojoFailureException {
        if (!enabled) return;

        try {
            processSQL();
        } catch (IOException e) {
            throw new MojoFailureException("Failed to generate the SQL migration script", e);
        }
    }


    private void processSQL() throws IOException {
        final Path outputFile = toPath(generationSqlFile);

        final StringBuilder result = new StringBuilder();

        final File[] sqlFiles = sortFiles(listFiles(migrationSqlDir));
        for (final File file : sqlFiles) {
            final Iterator<String> statements = toSqlStatementIterator(file);
            while (statements.hasNext()) {
                result.append(toJooqSafeStatement(statements.next()));
            }
        }

        Files.writeString(outputFile, result.toString(), UTF_8, CREATE, WRITE);
    }

    private static Path toPath(final String filename) throws IOException {
        if (filename == null || filename.isEmpty()) throw new FileNotFoundException("Generation SQL file is not specified");
        final File file = new File(filename);
        if (file.exists() && !file.delete()) throw new IOException("Generation SQL file exists and cannot be deleted");
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) throw new IOException("Could not create parent directories for generation SQL file");
        return file.toPath();
    }

    private static File[] listFiles(final String dirname) throws FileNotFoundException {
        if (dirname == null || dirname.isEmpty()) throw new FileNotFoundException("Specified Migration SQL directory is empty");
        final File dir = new File(dirname);
        if (!dir.exists()) throw new FileNotFoundException("Specified Migration SQL directory does not exist");
        if (!dir.isDirectory()) throw new FileNotFoundException("Specified Migration SQL directory is not a directory");
        return dir.listFiles();
    }

    private static File[] sortFiles(final File[] files) {
        // TODO find out of the default order is the correct order
        return files;
    }

    private static Iterator<String> toSqlStatementIterator(final File file) throws IOException {
        final String contents = Files.readString(file.toPath());
        return Arrays.stream(contents.split(";")).iterator();
    }

    private static final List<StatementParser> parsers = List.of(new ForeignKeyChecks(), new NameUTF8(), new CreateTable(), new AlterTable());

    private static String toJooqSafeStatement(final String statement) throws IOException {
        if (statement == null || statement.trim().isEmpty()) return "";
        for (final StatementParser parser : parsers) {
            if (!parser.matches(statement)) continue;
            return parser.convert(statement);
        }
        throw new IOException("No parser matched with statement: " + statement);
    }
}