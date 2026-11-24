package jooqpreprocessor.db.mysql;

import jooqpreprocessor.db.StatementParser;

import java.util.regex.Pattern;

/**
 * Drops MySQL {@code SET FOREIGN_KEY_CHECKS} statements, which jOOQ does not parse.
 */
public final class MyForeignKeyChecks implements StatementParser {

    /**
     * Creates the parser.
     */
    public MyForeignKeyChecks() {
    }

    private static final Pattern PATTERN = Pattern.compile("(SET|set) FOREIGN_KEY_CHECKS = [01]");

    @Override
    public boolean matches(final String statement) {
        return PATTERN.matcher(statement.trim()).matches();
    }

    @Override
    public String convert(final String statement) {
        return "";
    }
}
