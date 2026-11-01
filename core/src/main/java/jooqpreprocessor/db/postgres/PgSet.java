package jooqpreprocessor.db.postgres;

import jooqpreprocessor.db.StatementParser;

import java.util.HashMap;
import java.util.Map;

public class PgSet implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("SET ");
    }

    private final Map<String, String> currentValues = new HashMap<>();

    @Override
    public String convert(final String statement) {

        final int nameOffset = statement.indexOf(' ', 4);
        final int equalsOffset = statement.indexOf('=');
        if (nameOffset != -1 && equalsOffset != -1 && nameOffset < equalsOffset) {
            final String name = statement.substring(3, nameOffset).trim();
            final String value = statement.substring(equalsOffset+1).trim();
            if (value.equals(currentValues.get(name))) return "";

            currentValues.put(name, value);
        }

        return statement + ";\n";
    }

}
