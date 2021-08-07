package jooqpreprocessor.db.mysql;

import jooqpreprocessor.db.StatementParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static jooqpreprocessor.util.StringParsing.findNextSeparator;

public final class MyCreateTable implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("CREATE TABLE ");
    }

    @Override
    public String convert(final String statement) {
        final int startIndex = statement.indexOf('(');

        final List<String> middle = new LinkedList<>();
        for (String clause : toClausesList(toClausesOnlySection(statement))) {
            if (clause.isEmpty()) continue;
            if (clause.startsWith("KEY ")) continue;
            if (clause.startsWith("CONSTRAINT ")) continue;
            if (clause.startsWith("UNIQUE KEY ")) continue;

            clause = clause.replaceAll(" USING BTREE", "");
            clause = clause.replaceAll(" DEFAULT NULL", "");
            clause = clause.replaceAll(" DEFAULT 0", "");
            clause = clause.replaceAll(" DEFAULT b'0'", "");
            clause = clause.replaceAll(" DEFAULT b'1'", "");
            clause = clause.replaceAll(" COMMENT '[^']+'", "");

            middle.add(clause);
        }

        return statement.substring(0, startIndex)
            + "("
            + String.join(",", middle)
            + ");\n";
    }

    private static String toClausesOnlySection(final String createStatement) {
        final int startIndex = createStatement.indexOf('(');
        final int endIndex = createStatement.lastIndexOf(')');
        return createStatement.substring(startIndex+1, endIndex);
    }

    public static List<String> toClausesList(final String clauses) {
        final List<String> ret = new ArrayList<>();

        for (int start = 0, next; start != -1 && start < clauses.length();) {
            next = findNextSeparator(clauses, start);
            if (next == -1) {
                ret.add(clauses.substring(start).trim());
                start = next;
            } else {
                ret.add(clauses.substring(start, next).trim());
                start = next+1;
            }
        }

        return ret;
    }
}
