package jooqpreprocessor.bobplugin;

import bobthebuildtool.pojos.buildfile.Project;
import bobthebuildtool.pojos.error.VersionTooOld;
import bobthebuildtool.services.Log;
import jooqpreprocessor.codegen.CodegenArguments;
import jooqpreprocessor.codegen.JooqCodegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static bobthebuildtool.services.Functions.isNullOrEmpty;
import static bobthebuildtool.services.Update.requireBobVersion;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static jcli.CliParserBuilder.newCliParser;
import static jooqpreprocessor.codegen.CodegenArguments.DEFAULT_INPUT_LOCATION;

/**
 * The bob plugin: registers a {@code jooq-codegen} command that preprocesses the project's
 * migration scripts and then generates the jOOQ sources from them.
 */
public enum BobPlugin {;

    /**
     * Registers the {@code jooq-codegen} command on the given project.
     *
     * @param project the bob project the command is added to
     * @throws VersionTooOld when bob is older than the version this plugin needs
     */
    public static void installPlugin(final Project project) throws VersionTooOld {
        requireBobVersion("7");
        project.addCommand("jooq-codegen", "Generates JOOQ code", BobPlugin::generateJooqCode);
    }

    private static int generateJooqCode(final Project project, final Map<String, String> env, final String[] args) throws Exception {
        final CodegenArguments arguments = newCliParser(CodegenArguments::new)
            .onErrorPrintHelpAndExit()
            .onHelpPrintHelpAndExit()
            .parse(args);

        if (!isEnabled(project, arguments)) return 0;

        final String generatedSourceDir = toGeneratedSourceDir(project, arguments);
        if (project.config.sourceDirectories.isEmpty()) {
            addIfAbsent(project.config.sourceDirectories, generatedSourceDir);
            addIfAbsent(project.config.sourceDirectories, "src" + File.separator + "main" + File.separator + "java");
        } else {
            addIfAbsent(project.config.sourceDirectories, 0, generatedSourceDir);
        }

        String inputLocation = arguments.inputScript;
        if (!arguments.dontPreProcess) {
            final Path inputDir = toInputDir(project, arguments);
            final Path outputFile = project.getBuildTarget().resolve(arguments.generationSqlFile);
            JooqCodegen.preprocess(Log::logWarning, inputDir, outputFile, arguments.dbType);
            inputLocation = outputFile.toAbsolutePath().toString();
        }

        final String packageName = toPackageName(project, arguments);
        final String outputDir = toGeneratedSourceDir(project, arguments);
        JooqCodegen.generate(arguments, inputLocation, outputDir, packageName);
        return 0;
    }

    // A project without migration scripts in the default location is left alone, so that
    // installing the plugin never breaks a build that does not use it.
    private static boolean isEnabled(final Project project, final CodegenArguments arguments) {
        if (!DEFAULT_INPUT_LOCATION.equals(arguments.inputScript)) return true;
        return exists(project.parentDir.resolve(DEFAULT_INPUT_LOCATION));
    }

    private static Path toInputDir(final Project project, final CodegenArguments arguments) throws FileNotFoundException {
        if (isNullOrEmpty(arguments.inputScript)) throw new FileNotFoundException("Specified Migration SQL directory is empty");
        final Path inputDir = project.parentDir.resolve(arguments.inputScript);

        if (!exists(inputDir)) throw new FileNotFoundException("Specified Migration SQL directory does not exist");
        if (!isDirectory(inputDir)) throw new FileNotFoundException("Specified Migration SQL directory is not a directory");

        return inputDir;
    }

    private static String toPackageName(final Project project, final CodegenArguments arguments) {
        return isNullOrEmpty(arguments.packageName) ? project.config.name + ".schema" : arguments.packageName;
    }

    private static String toGeneratedSourceDir(final Project project, final CodegenArguments arguments) {
        return project.parentDir.relativize(project.getBuildTarget().resolve(arguments.outputDir)).toString();
    }

    private static void addIfAbsent(final List<String> sourceDirectories, final String dir) {
        if (!sourceDirectories.contains(dir)) sourceDirectories.add(dir);
    }

    private static void addIfAbsent(final List<String> sourceDirectories, final int index, final String dir) {
        if (!sourceDirectories.contains(dir)) sourceDirectories.add(index, dir);
    }

}
