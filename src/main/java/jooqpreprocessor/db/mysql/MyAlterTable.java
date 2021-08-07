package jooqpreprocessor.db.mysql;

import jooqpreprocessor.db.StatementParser;

import java.util.*;

import static jooqpreprocessor.util.StringParsing.findNextSeparator;

public final class MyAlterTable implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.startsWith("ALTER TABLE `");
    }

    @Override
    public String convert(final String statement) {
        // Remove the ALTER TABLE part
        final int startIndex = statement.indexOf('`', 13);
        final String alterStart = statement.substring(0, startIndex+1);

        final List<String> alters = new LinkedList<>();
        for (String clause : toClausesList(toClausesOnlySection(statement))) {
            if (clause.isEmpty()) continue;

            // Removing indexes and constraints, JOOQ does not need to know about these
            if (clause.startsWith("ADD INDEX ")) continue;
            if (clause.startsWith("ADD CONSTRAINT ")) continue;
            if (clause.startsWith("ADD UNIQUE INDEX ")) continue;
            if (clause.startsWith("DROP INDEX ")) continue;
            if (clause.startsWith("DROP CONSTRAINT ")) continue;
            if (clause.startsWith("DROP FOREIGN KEY ")) continue;

            // Dropping a primary key might be poorly ordered, putting it first fixes the problem
            if (clause.equals("DROP PRIMARY KEY")) {
                alters.add(0, alterStart + " " + clause.trim() + ";");
                continue;
            }

            // The complicated stuff, removing parts of clauses that JOOQ can't handle
            clause = clause.replaceAll("( AUTO_INCREMENT| auto_increment)", "");
            clause = clause.replaceAll("( FIRST| first)", "");
            clause = clause.replaceAll("( USING BTREE| using btree)", "");
            // DEFAULT clauses are supported in ADD COLUMN but not in MODIFY COLUMN
            clause = clause.replaceAll(" DEFAULT NULL", "");
            clause = clause.replaceAll(" DEFAULT 0", "");
            clause = clause.replaceAll(" DEFAULT b'0'", "");
            clause = clause.replaceAll(" DEFAULT b'1'", "");
            clause = clause.replaceAll(" COMMENT '[^']+'", "");

            int afterIndex = clause.indexOf(" AFTER ");
            if (afterIndex != -1) {
                int endfieldIndex = clause.indexOf('`', afterIndex + 8);
                clause = clause.substring(0, afterIndex) + " " + clause.substring(endfieldIndex + 1);
            }

            // add whats left of the clause
            alters.add(alterStart + " " + clause.trim() + ";");
        }

        return alters.isEmpty() ? "" : String.join("\n", alters)+"\n";
    }

    private static String toClausesOnlySection(final String alterStatement) {
        // 13 is the offset that starts after "ALTER TABLE `"
        final int startIndex = alterStatement.indexOf('`', 13);
        // 2 removes the last ` and the space following it
        return alterStatement.trim().substring(startIndex+2);
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
