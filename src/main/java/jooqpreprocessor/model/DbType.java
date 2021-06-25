package jooqpreprocessor.model;

import jooqpreprocessor.mysql.*;
import jooqpreprocessor.postgres.PgAlterTable;
import jooqpreprocessor.postgres.PgCreateSchema;
import jooqpreprocessor.postgres.PgCreateTable;
import jooqpreprocessor.postgres.PgSet;

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
