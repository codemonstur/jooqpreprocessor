package jooqpreprocessor.db.postgres;

import jooqpreprocessor.db.StatementParser;

/**
 * Passes PostgreSQL {@code CREATE SCHEMA} statements through with a terminating semicolon.
 */
public class PgCreateSchema implements StatementParser {

    /**
     * Creates the parser.
     */
    public PgCreateSchema() {
    }

    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("CREATE SCHEMA ");
    }

    @Override
    public String convert(final String statement) {
        return statement + ";\n";
    }
}
