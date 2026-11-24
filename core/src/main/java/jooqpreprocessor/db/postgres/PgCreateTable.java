package jooqpreprocessor.db.postgres;

import jooqpreprocessor.db.StatementParser;

/**
 * Passes PostgreSQL {@code CREATE TABLE} statements through with a terminating semicolon.
 */
public class PgCreateTable implements StatementParser {

    /**
     * Creates the parser.
     */
    public PgCreateTable() {
    }

    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("CREATE TABLE ");
    }

    @Override
    public String convert(final String statement) {
        return statement + ";\n";
    }

}
