package jooqpreprocessor.model;

import jooqpreprocessor.db.StatementParser;
import jooqpreprocessor.db.mysql.*;
import jooqpreprocessor.db.postgres.PgAlterTable;
import jooqpreprocessor.db.postgres.PgCreateSchema;
import jooqpreprocessor.db.postgres.PgCreateTable;
import jooqpreprocessor.db.postgres.PgSet;

import java.util.Arrays;
import java.util.List;

/**
 * The database dialect the migration scripts are written in.
 */
public enum DbType {

    /** MySQL and MariaDB. */
    mysql,

    /** PostgreSQL. */
    postgres;

    /**
     * Returns the statement parsers that rewrite this dialect's statements into
     * jOOQ-parsable SQL.
     *
     * @return the parsers of this dialect, in the order they are applied
     */
    public List<StatementParser> listStatementParsers() {
        switch (this) {
            case mysql: return Arrays.asList( new MyForeignKeyChecks()
                , new MyNameUTF8(), new MyCreateTable(), new MyAlterTable()
                , new MyDropTable(), new MyCreateIndex() );
            case postgres: return Arrays.asList( new PgCreateSchema()
                , new PgCreateTable(), new PgAlterTable(), new PgSet() );
        }
        throw new IllegalArgumentException();
    }

}
