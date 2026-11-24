package jooqpreprocessor.db.mysql;

import jooqpreprocessor.db.StatementParser;

/**
 * Drops the MySQL {@code SET NAMES utf8mb4} statement, which jOOQ does not parse.
 */
public final class MyNameUTF8 implements StatementParser {

    /**
     * Creates the parser.
     */
    public MyNameUTF8() {
    }

    @Override
    public boolean matches(final String statement) {
        return statement.trim().equals("SET NAMES utf8mb4");
    }

    @Override
    public String convert(final String statement) {
        return "";
    }
}
