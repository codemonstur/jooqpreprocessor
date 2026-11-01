package jooqpreprocessor.db.mysql;

import jooqpreprocessor.db.StatementParser;

public final class MyNameUTF8 implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.trim().equals("SET NAMES utf8mb4");
    }

    @Override
    public String convert(final String statement) {
        return "";
    }
}
