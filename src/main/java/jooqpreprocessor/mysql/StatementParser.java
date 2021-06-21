package jooqpreprocessor.mysql;

public interface StatementParser {
    boolean matches(String statement);
    String convert(String statement);
}
