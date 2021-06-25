package jooqpreprocessor;

import jooqpreprocessor.model.DbType;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static jooqpreprocessor.MigrationsProcessor.processSQL;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_RESOURCES;

@Mojo( defaultPhase = GENERATE_RESOURCES, name = "generate" )
public final class MavenGenerateJooqSql extends AbstractMojo {

    @Parameter(defaultValue = "true")
    public boolean enabled;
    @Parameter(defaultValue = "src/main/resources/db/migration")
    public String migrationSqlDir;
    @Parameter(defaultValue = "target/generated-resources/db/schema.sql")
    public String generationSqlFile;
    @Parameter(defaultValue = "mysql")
    public DbType dbType;

    public void execute() throws MojoFailureException {
        if (!enabled) return;

        try {
            // Don't need this until I start writing the results but I want the validation checks to run first.
            final Path outputFile = toPath(generationSqlFile);

            final String result = processSQL(getLog()::warn, migrationSqlDir, dbType.listStatementParsers());

            Files.write(outputFile, result.getBytes(UTF_8), CREATE, WRITE);
        } catch (IOException e) {
            throw new MojoFailureException("Failed to generate the SQL migration script", e);
        }
    }

    private static Path toPath(final String filename) throws IOException {
        if (filename == null || filename.isEmpty()) throw new FileNotFoundException("Generation SQL file is not specified");
        final File file = new File(filename);
        if (file.exists() && !file.delete()) throw new IOException("Generation SQL file exists and cannot be deleted");
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) throw new IOException("Could not create parent directories for generation SQL file");
        return file.toPath();
    }

}