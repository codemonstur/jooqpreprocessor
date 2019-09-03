package jooqpreprocessor.parsers;

import java.util.regex.Pattern;

public final class ForeignKeyChecks implements StatementParser {

    private static final Pattern PATTERN = Pattern.compile("(SET|set) FOREIGN_KEY_CHECKS = [01]");

    @Override
    public boolean matches(final String statement) {
        return PATTERN.matcher(statement.trim()).matches();
    }

    @Override
    public String convert(final String statement) {
        return "";
    }
}
