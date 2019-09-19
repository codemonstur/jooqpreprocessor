package jooqpreprocessor.parsers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class AlterTable implements StatementParser {

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
        final Iterator<String> clauses = Arrays
            .stream(statement.trim().substring(startIndex+2).split(","))
            .iterator();
        while (clauses.hasNext()) {
            String clause = clauses.next().trim();
            if (clause.isEmpty()) continue;

            // Removing indexes and constraints, JOOQ does not need to know about these
            if (clause.startsWith("ADD INDEX ")) continue;
            if (clause.startsWith("ADD CONSTRAINT ")) continue;
            if (clause.startsWith("ADD UNIQUE INDEX ")) continue;
            if (clause.startsWith("DROP INDEX ")) continue;
            if (clause.startsWith("DROP CONSTRAINT ")) continue;

            // Dropping a primary key might be poorly ordered, putting it first fixes the problem
            if (clause.equals("DROP PRIMARY KEY")) {
                alters.add(0, alterStart + " " + clause.trim() + ";");
                continue;
            }

            // The complicated stuff, removing parts of clauses that JOOQ can't handle
            clause = clause.replaceAll("( AUTO_INCREMENT| auto_increment)", "");
            clause = clause.replaceAll("( FIRST| first)", "");
            clause = clause.replaceAll("( USING BTREE| using btree)", "");
            clause = clause.replaceAll(" DEFAULT NULL", "");
            int afterIndex = clause.indexOf(" AFTER ");
            if (afterIndex != -1) {
                int endfieldIndex = clause.indexOf('`', afterIndex+8);
                clause = clause.substring(0, afterIndex) + " " + clause.substring(endfieldIndex+1);
            }

            // add whats left of the clause
            alters.add(alterStart + " " + clause.trim() + ";");
        }

        return alters.isEmpty() ? "" : String.join("\n", alters)+"\n";
    }

}
