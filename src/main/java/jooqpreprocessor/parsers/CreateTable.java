package jooqpreprocessor.parsers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class CreateTable implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.trim().startsWith("CREATE TABLE ");
    }

    @Override
    public String convert(final String statement) {
        final int startIndex = statement.indexOf('(');
        final int endIndex = statement.lastIndexOf(')');

        final List<String> middle = new LinkedList<>();
        final Iterator<String> middlePart = Arrays
            .stream(statement.substring(startIndex+1, endIndex).split(","))
            .iterator();
        while (middlePart.hasNext()) {
            final String part = middlePart.next().trim();
            if (part.startsWith("KEY ") || part.startsWith("CONSTRAINT ")) continue;
            middle.add(part);
        }

        return statement.substring(0, startIndex)
            + "("
            + String.join(",", middle)
            + ")";
    }

}
