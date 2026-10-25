package jooqpreprocessor.db.postgres;

import jooqpreprocessor.db.StatementParser;

public class PgCreateTable implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("CREATE TABLE ");
    }

    @Override
    public String convert(final String statement) {
        return statement + ";\n";
    }

}
