package jooqpreprocessor.db.postgres;

import jooqpreprocessor.db.StatementParser;

public class PgCreateSchema implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("CREATE SCHEMA ");
    }

    @Override
    public String convert(final String statement) {
        return statement + ";\n";
    }
}
