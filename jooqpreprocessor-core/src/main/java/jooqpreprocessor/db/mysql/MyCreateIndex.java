package jooqpreprocessor.db.mysql;

import jooqpreprocessor.db.StatementParser;

public final class MyCreateIndex implements StatementParser {

    @Override public boolean matches(final String statement) {
        return statement.startsWith("CREATE INDEX ");
    }

    @Override public String convert(final String statement) {
        return "";
    }

}
