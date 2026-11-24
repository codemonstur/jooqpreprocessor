package jooqpreprocessor.db.mysql;

import jooqpreprocessor.db.StatementParser;

/**
 * Drops MySQL {@code CREATE INDEX} statements: jOOQ does not parse them, and indexes are not
 * needed to generate code.
 */
public final class MyCreateIndex implements StatementParser {

    /**
     * Creates the parser.
     */
    public MyCreateIndex() {
    }

    @Override public boolean matches(final String statement) {
        return statement.startsWith("CREATE INDEX ");
    }

    @Override public String convert(final String statement) {
        return "";
    }

}
