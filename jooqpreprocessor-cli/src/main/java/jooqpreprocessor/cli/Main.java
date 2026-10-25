package jooqpreprocessor.cli;

import jooqpreprocessor.codegen.CodegenArguments;
import jooqpreprocessor.codegen.JooqCodegen;

import java.nio.file.Files;
import java.nio.file.Path;

import static jcli.CliParserBuilder.newCliParser;

/**
 * The jooqpreprocessor command line entry point: preprocesses the migration scripts into
 * jOOQ-parsable SQL and runs the jOOQ code generator over them, without Maven or bob.
 */
public final class Main {

    /**
     * Parses the command line, preprocesses the migration scripts and generates the jOOQ
     * sources. Any failure prints a single line to stderr and exits with status 1.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        try {
            final CodegenArguments arguments = newCliParser(CodegenArguments::new)
                .onHelpPrintHelpAndExit()
                .onErrorPrintHelpAndExit()
                .parse(args);

            run(arguments);
        } catch (final Exception e) {
            System.err.println("jooqpreprocessor: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void run(final CodegenArguments arguments) throws Exception {
        final Path inputDir = Path.of(arguments.inputScript);
        if (!Files.isDirectory(inputDir))
            throw new IllegalArgumentException("Migration SQL directory does not exist: " + arguments.inputScript);

        final String inputLocation;
        if (arguments.dontPreProcess) {
            inputLocation = arguments.inputScript;
        } else {
            final Path schemaFile = Path.of(arguments.generationSqlFile);
            JooqCodegen.preprocess(message -> System.err.println("[warn] " + message),
                inputDir, schemaFile, arguments.dbType);
            inputLocation = schemaFile.toAbsolutePath().toString();
        }

        JooqCodegen.generate(arguments, inputLocation, arguments.outputDir, toPackageName(arguments));
    }

    // The bob plugin derives this from the project name, which a command line tool does not have.
    private static String toPackageName(final CodegenArguments arguments) {
        return arguments.packageName == null || arguments.packageName.isBlank()
            ? "schema" : arguments.packageName;
    }

}
