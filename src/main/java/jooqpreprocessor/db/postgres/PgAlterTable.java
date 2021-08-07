package jooqpreprocessor.db.postgres;

import jooqpreprocessor.db.StatementParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static jooqpreprocessor.util.StringParsing.findNextSeparator;

public final class PgAlterTable implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("ALTER TABLE ");
    }

    @Override
    public String convert(final String statement) {
        final int startIndex = statement.indexOf(' ', 13);
        final String alterStart = statement.substring(0, startIndex);

        final List<String> alters = new LinkedList<>();
        for (String clause : toClausesList(toClausesOnlySection(statement))) {
            if (clause.isEmpty()) continue;
            if (clause.contains("ADD GENERATED ALWAYS AS IDENTITY")) continue;
            if (clause.startsWith("ADD CONSTRAINT")) continue;

            clause = clause.replaceAll("USING [a-zA-Z_]+::bigint", "").trim();
            clause = clause.replaceAll("USING [a-zA-Z_]+::character varying\\([0-9]+\\)", "").trim();

            alters.add(alterStart + " " + clause);
        }

        if (alters.isEmpty()) return "";
        return String.join(";\n", alters) + ";\n";
    }

    private static String toClausesOnlySection(final String alterStatement) {
        // 12 is the offset that starts after "ALTER TABLE "
        final int startIndex = alterStatement.indexOf(' ', 12);
        if (startIndex == -1) throw new IllegalArgumentException("Malformed SQL, no table name found in ALTER statement");
        // 1 removes the space following it
        return alterStatement.substring(startIndex+1);
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
