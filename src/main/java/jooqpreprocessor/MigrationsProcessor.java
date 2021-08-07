package jooqpreprocessor;

import jooqpreprocessor.db.StatementParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;

public class MigrationsProcessor {

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

    private static int toNumber(final String name) {
        final int periodIndex = name.indexOf('.');
        final int underscoreIndex = name.indexOf("__");
        if ( periodIndex == -1 || underscoreIndex == -1 ) return 0;

        try {
            return Integer.parseInt(name.substring(periodIndex+1, underscoreIndex));
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
