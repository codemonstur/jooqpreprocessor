package jooqpreprocessor.maven;

import jooqpreprocessor.model.DbType;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Path;

import static jooqpreprocessor.MigrationsProcessor.processSQL;
import static jooqpreprocessor.MigrationsProcessor.toSchemaFile;
import static jooqpreprocessor.MigrationsProcessor.writeSchemaFile;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_RESOURCES;

/**
 * Rewrites the migration scripts into the single, jOOQ-parsable schema file that the jOOQ
 * code generator needs — so one set of scripts can drive both the database and the generator.
 * <p>
 * The generated file is meant to be handed to jOOQ's own plugin, which is why this goal only
 * preprocesses and never generates code itself.
 */
@Mojo( defaultPhase = GENERATE_RESOURCES, name = "generate" )
public final class MavenGenerateJooqSql extends AbstractMojo {

    /** Whether this goal runs at all. */
    @Parameter(defaultValue = "true")
    public boolean enabled;

    /** Directory holding the SQL migration scripts. */
    @Parameter(defaultValue = "src/main/resources/db/migration")
    public String migrationSqlDir;

    /** File the preprocessed, jOOQ-parsable SQL is written to. */
    @Parameter(defaultValue = "target/generated-resources/db/schema.sql")
    public String generationSqlFile;

    /** Dialect the migration scripts are written in. */
    @Parameter(defaultValue = "mysql")
    public DbType dbType;

    /**
     * Validates the configuration, preprocesses the migration scripts and writes the schema
     * file.
     *
     * @throws MojoFailureException when the scripts cannot be read or the file cannot be written
     */
    public void execute() throws MojoFailureException {
        if (!enabled) return;

        try {
            // Don't need this until I start writing the results but I want the validation checks to run first.
            final Path outputFile = toSchemaFile(generationSqlFile);

            final String result = processSQL(getLog()::warn, migrationSqlDir, dbType.listStatementParsers());

            writeSchemaFile(outputFile, result);
        } catch (IOException e) {
            throw new MojoFailureException("Failed to generate the SQL migration script", e);
        }
    }

}
