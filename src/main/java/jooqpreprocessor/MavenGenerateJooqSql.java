package jooqpreprocessor;

import jooqpreprocessor.parsers.*;
import org.apache.log4j.Level;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_RESOURCES;

@Mojo( defaultPhase = GENERATE_RESOURCES, name = "generate" )
public final class MavenGenerateJooqSql extends AbstractMojo {
    static {
        org.apache.log4j.LogManager.getRootLogger().setLevel(Level.OFF);
    }

    @Parameter(defaultValue = "true")
    public boolean enabled;

    @Parameter(defaultValue = "src/main/resources/db/migration")
    public String migrationSqlDir;

    @Parameter(defaultValue = "target/generated-resources/db/schema.sql")
    public String generationSqlFile;

    private static final List<StatementParser> parsers = Arrays.asList(new ForeignKeyChecks()
            , new NameUTF8(), new CreateTable(), new AlterTable(), new DropTable());

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
        Log log = getLog();

        final String result = Arrays
            .stream(listFiles(migrationSqlDir)
            .filter(File::isFile)
            .sorted(comparingInt(o -> toNumber(o.getName())))
            .flatMap(MavenGenerateJooqSql::readAllLines)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .filter(s -> !s.startsWith("--"))
            .map(s -> s+" ")
            .collect(joining())
            .split(";"))
            .filter(Objects::nonNull)
            .filter(s -> !s.trim().isEmpty())
            .map(String::trim)
            .map(statement -> toJooqSafeStatement(log, statement))
            .collect(joining());

        Files.write(outputFile, result.getBytes(UTF_8), CREATE, WRITE);
    }

    private static Path toPath(final String filename) throws IOException {
        if (filename == null || filename.isEmpty()) throw new FileNotFoundException("Generation SQL file is not specified");
        final File file = new File(filename);
        if (file.exists() && !file.delete()) throw new IOException("Generation SQL file exists and cannot be deleted");
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) throw new IOException("Could not create parent directories for generation SQL file");
        return file.toPath();
    }

    private static Stream<File> listFiles(final String dirname) throws FileNotFoundException {
        if (dirname == null || dirname.isEmpty()) throw new FileNotFoundException("Specified Migration SQL directory is empty");
        final File dir = new File(dirname);
        if (!dir.exists()) throw new FileNotFoundException("Specified Migration SQL directory does not exist");
        if (!dir.isDirectory()) throw new FileNotFoundException("Specified Migration SQL directory is not a directory");
        return Arrays.stream(dir.listFiles());
    }

    private static Stream<String> readAllLines(final File path) {
        try {
            return Files.readAllLines(path.toPath()).stream();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static int toNumber(final String name) {
        final int periodIndex = name.indexOf('.');
        final int underscoreIndex = name.indexOf("__");
        return
            ( periodIndex == -1 || underscoreIndex == -1 )
            ? 0
            : parseInt(name.substring(periodIndex+1, underscoreIndex), 0);
    }
    private static int parseInt(final String value, final int _default) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return _default;
        }
    }

    private static String toJooqSafeStatement(final Log log, final String statement) {
        for (final StatementParser parser : parsers) {
            if (parser.matches(statement))
                return parser.convert(statement);
        }
        log.warn("No parser matched with statement: " + statement);
        return statement;
    }
}