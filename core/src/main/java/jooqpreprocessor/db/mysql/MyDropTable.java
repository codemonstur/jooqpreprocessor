package jooqpreprocessor.db.mysql;

import jooqpreprocessor.db.StatementParser;

/**
 * Passes MySQL {@code DROP TABLE} statements through with a terminating semicolon.
 */
public final class MyDropTable implements StatementParser {

    /**
     * Creates the parser.
     */
    public MyDropTable() {
    }

    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("DROP TABLE `");
    }

    @Override
    public String convert(final String statement) {
        return statement+";\n";
    }
}
