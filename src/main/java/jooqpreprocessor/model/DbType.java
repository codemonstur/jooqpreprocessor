package jooqpreprocessor.model;

import jooqpreprocessor.db.StatementParser;
import jooqpreprocessor.db.mysql.*;
import jooqpreprocessor.db.postgres.PgAlterTable;
import jooqpreprocessor.db.postgres.PgCreateSchema;
import jooqpreprocessor.db.postgres.PgCreateTable;
import jooqpreprocessor.db.postgres.PgSet;

import java.util.Arrays;
import java.util.List;

public enum DbType {
    mysql,
    postgres;

    public List<StatementParser> listStatementParsers() {
        switch (this) {
            case mysql: return Arrays.asList( new MyForeignKeyChecks()
                , new MyNameUTF8(), new MyCreateTable(), new MyAlterTable()
                , new MyDropTable() );
            case postgres: return Arrays.asList( new PgCreateSchema()
                , new PgCreateTable(), new PgAlterTable(), new PgSet() );
        }
        throw new IllegalArgumentException();
    }

}
