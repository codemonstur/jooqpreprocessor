package jooqpreprocessor.codegen;

import jooqpreprocessor.MigrationsProcessor;
import jooqpreprocessor.model.DbType;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Property;
import org.jooq.meta.jaxb.Target;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import static jooqpreprocessor.MigrationsProcessor.processSQL;
import static jooqpreprocessor.MigrationsProcessor.writeSchemaFile;

/**
 * Preprocesses migration scripts into jOOQ-parsable SQL and runs the jOOQ code generator
 * over the result.
 * <p>
 * This is the half of the tool that both the bob plugin and the command line tool need. The
 * Maven plugin only preprocesses — jOOQ's own plugin does the code generation — which is why
 * jOOQ itself is an optional dependency of the core module and is not inherited by it.
 */
public enum JooqCodegen {;

    /**
     * Reads every migration script in {@code inputDir}, rewrites the statements jOOQ cannot
     * parse, and writes the result to {@code outputFile}.
     *
     * @param log receives a warning for every statement no parser matched
     * @param inputDir directory holding the migration scripts
     * @param outputFile the schema file to write, replaced if it already exists
     * @param dbType dialect the migration scripts are written in
     * @throws IOException when the scripts cannot be read or the file cannot be written
     */
    public static void preprocess(final Consumer<String> log, final Path inputDir
            , final Path outputFile, final DbType dbType) throws IOException {
        final String sql = processSQL(log, inputDir.toString(), dbType.listStatementParsers());
        writeSchemaFile(outputFile, sql);
    }

    /**
     * Runs the jOOQ code generator over a single, already jOOQ-parsable SQL script.
     *
     * @param arguments the generator options
     * @param inputLocation the SQL jOOQ parses — the preprocessed schema, or the raw scripts
     * @param outputDir directory the generated sources are written to
     * @param packageName package the generated sources are placed in
     * @throws Exception when jOOQ fails to generate
     */
    public static void generate(final CodegenArguments arguments, final String inputLocation
            , final String outputDir, final String packageName) throws Exception {
        GenerationTool.generate(newConfiguration(arguments, inputLocation, outputDir, packageName));
    }

    private static Configuration newConfiguration(final CodegenArguments arguments
            , final String inputLocation, final String outputDir, final String packageName) {

        return new Configuration()
            .withLogging(arguments.logging)
            .withGenerator(new Generator()
                .withGenerate(new Generate()
                    .withJavadoc(arguments.withJavadoc)
                    .withComments(arguments.withComments)
                    .withEmptySchemas(arguments.withEmptySchemas)
                    .withEmptyCatalogs(arguments.withEmptyCatalogs)
                    .withGeneratedAnnotation(arguments.withGeneratedAnnotation)
                    .withGlobalCatalogReferences(arguments.withGlobalCatalogReferences)
                    .withDaos(arguments.withDaos)
                    .withPojos(arguments.withPojos)
                    .withIndexes(arguments.withIndexes)
                    .withKeys(arguments.withKeys)
                    .withInstanceFields(arguments.withInstanceFields)
                    .withRecords(arguments.withRecords))
                .withDatabase(new Database()
                    .withName("org.jooq.meta.extensions.ddl.DDLDatabase")
                    .withProperties(new Property()
                        .withKey("scripts")
                        .withValue(inputLocation)))
                .withTarget(new Target()
                    .withPackageName(packageName)
                    .withDirectory(outputDir)));
    }

}
