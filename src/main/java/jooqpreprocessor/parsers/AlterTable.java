package jooqpreprocessor.parsers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class AlterTable implements StatementParser {

    @Override
    public boolean matches(final String statement) {
        return statement.trim().startsWith("ALTER TABLE `");
    }

    @Override
    public String convert(final String statement) {
        final int startIndex = statement.trim().indexOf('`', 13);
        final String alterStart = statement.trim().substring(0, startIndex+1);

        final List<String> alters = new LinkedList<>();
        final Iterator<String> clauses = Arrays
            .stream(statement.trim().substring(startIndex+2).split(","))
            .iterator();
        while (clauses.hasNext()) {
            final String clause = clauses.next().trim();
            if (clause.startsWith("ADD INDEX ")) continue;
            if (clause.startsWith("ADD CONSTRAINT ")) continue;

            alters.add(alterStart + " " + clause + ";");
        }

        return String.join("\n", alters)+"\n";
    }

    // ALTER TABLE `invitation`
    // ADD COLUMN `wedding_id` int(10) unsigned NOT NULL AFTER `invitation_id`,
    // ADD INDEX `fk_invitation_wedding_idx` (`wedding_id`) USING BTREE,
    // ADD CONSTRAINT `fk_invitation_wedding` FOREIGN KEY `fk_invitation_wedding` (`wedding_id`) REFERENCES `wedding` (`wedding_id`) ON DELETE CASCADE ON UPDATE CASCADE

}
