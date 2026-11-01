package jooqpreprocessor.db.mysql;

import jooqpreprocessor.db.StatementParser;

public final class MyDropTable implements StatementParser {
    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("DROP TABLE `");
    }

    @Override
    public String convert(final String statement) {
        return statement+";\n";
    }
}
