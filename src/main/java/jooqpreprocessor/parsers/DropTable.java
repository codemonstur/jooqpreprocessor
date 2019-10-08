package jooqpreprocessor.parsers;

public final class DropTable implements StatementParser {
    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("DROP TABLE `");
    }

    @Override
    public String convert(final String statement) {
        return statement;
    }
}
