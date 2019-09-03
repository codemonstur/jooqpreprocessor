package jooqpreprocessor.parsers;

public final class NameUTF8 implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.trim().equals("SET NAMES utf8mb4");
    }

    @Override
    public String convert(final String statement) {
        return "";
    }
}
