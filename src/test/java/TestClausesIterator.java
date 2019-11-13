import jooqpreprocessor.parsers.AlterTable;

import java.util.List;

public class TestClausesIterator {

    public static void main(final String... args) {
        final List<String> strings = AlterTable.toClausesList("MODIFY COLUMN `type` int(11) NOT NULL COMMENT 'filldb enum \\'1,2' AFTER `organization_id`, MODIFY COLUMN `difficulty` int(10) unsigned NOT NULL COMMENT 'filldb enum 1,2,3' AFTER `name`");
        int i = 0;
        for (final String s : strings) {
            System.out.println(i++ + ": " + s);
        }
    }

}
