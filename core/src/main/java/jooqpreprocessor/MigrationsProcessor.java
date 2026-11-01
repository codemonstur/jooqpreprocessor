package jooqpreprocessor;

import jooqpreprocessor.db.StatementParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;

public class MigrationsProcessor {

    /**
     * Reads every migration script in the given directory, in migration order, strips the
     * constructs jOOQ cannot parse, and returns the result as a single jOOQ-parsable script.
     *
     * @param log receives a warning for every statement no parser matched
     * @param migrationSqlDir directory holding the migration scripts
     * @param parsers the statement parsers of the dialect the scripts are written in
     * @return the merged, jOOQ-parsable SQL
     * @throws IOException when the directory or one of its files cannot be read
     */
    public static String processSQL(final Consumer<String> log, final String migrationSqlDir, final List<StatementParser> parsers) throws IOException {
        final String[] statements = removeComments(listFiles(migrationSqlDir)
            .filter(File::isFile)
            .sorted(comparingInt(o -> toNumber(o.getName())))
            .flatMap(MigrationsProcessor::readAllLines)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .filter(s -> !s.startsWith("--"))
            .map(s -> s+" ")
            .collect(joining()))
            // FIXME this split operation is dangerous, what if there is a semicolon that isn't the end of a statement?
            .split(";");
        return Arrays
            .stream(statements)
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(statement -> !statement.isEmpty())
            .map(statement -> toJooqSafeStatement(log, parsers, statement))
            .collect(joining());
    }

    /**
     * Prepares the schema file named by the given path: the name must be present, an already
     * existing file is deleted and missing parent directories are created.
     *
     * @param filename the path of the schema file to write
     * @return the same path, ready to be written to
     * @throws FileNotFoundException when no filename is specified
     * @throws IOException when an existing file cannot be removed or the directories cannot be created
     */
    public static Path toSchemaFile(final String filename) throws IOException {
        if (filename == null || filename.isEmpty())
            throw new FileNotFoundException("Generation SQL file is not specified");
        return toSchemaFile(Path.of(filename));
    }

    /**
     * Prepares the file the preprocessed SQL is written to: the path must be present, an
     * already existing file is deleted and missing parent directories are created.
     *
     * @param outputFile the schema file that will be written
     * @return the same path, ready to be written to
     * @throws FileNotFoundException when no output file is specified
     * @throws IOException when an existing file cannot be removed or the directories cannot be created
     */
    public static Path toSchemaFile(final Path outputFile) throws IOException {
        if (outputFile == null || outputFile.toString().isEmpty())
            throw new FileNotFoundException("Generation SQL file is not specified");
        final File file = outputFile.toFile();
        if (file.exists() && !file.delete()) throw new IOException("Generation SQL file exists and cannot be deleted");
        final File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs())
            throw new IOException("Could not create parent directories for generation SQL file");
        return outputFile;
    }

    /**
     * Writes preprocessed SQL to the schema file, replacing that file if it already exists.
     *
     * @param outputFile the schema file to write
     * @param sql the preprocessed SQL
     * @throws IOException when the file cannot be prepared or written
     */
    public static void writeSchemaFile(final Path outputFile, final String sql) throws IOException {
        Files.writeString(toSchemaFile(outputFile), sql);
    }

    private static String removeComments(final String line) {
        final StringBuilder ret = new StringBuilder();
        int i = 0;
        while (i < line.length()) {
            int offset = line.indexOf("/*", i);
            if (offset == -1) {
                ret.append(line.substring(i));
                i = line.length();
            } else {
                ret.append(line, i, offset);
                int closeOffset = line.indexOf("*/", offset);
                if (closeOffset == -1) break;
                i = closeOffset + 2;
            }
        }
        return ret.toString();
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

    // Sorts the migration scripts the way Flyway orders them: by the version between the
    // leading V and the "__" separator. Handles "V1__orders.sql" as well as "V1.10__after.sql",
    // where the minor version has to stay ahead of "V1.9__using.sql" — and anything that is not
    // a version number at all sorts first, unparsed.
    private static int toNumber(final String name) {
        final int underscoreIndex = name.indexOf("__");
        if ( underscoreIndex == -1 ) return 0;

        final String version = name.startsWith("V")
            ? name.substring(1, underscoreIndex)
            : name.substring(0, underscoreIndex);
        final int dotIndex = version.indexOf('.');

        try {
            final int major = Integer.parseInt(version.substring(0, dotIndex == -1 ? version.length() : dotIndex));
            final int minor = dotIndex == -1 ? 0 : Integer.parseInt(version.substring(dotIndex + 1));
            return major * 1000 + minor;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String toJooqSafeStatement(final Consumer<String> log, final List<StatementParser> parsers
            , final String statement) {
        for (final StatementParser parser : parsers) {
            if (parser.matches(statement))
                return parser.convert(statement);
        }
        log.accept("No parser matched with statement: " + statement);
        return statement;
    }

}
