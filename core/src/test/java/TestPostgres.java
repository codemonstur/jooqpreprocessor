import java.io.IOException;

import static jooqpreprocessor.MigrationsProcessor.processSQL;
import static jooqpreprocessor.model.DbType.postgres;

public class TestPostgres {

    public static void main(final String... args) throws IOException {
        final String sql = processSQL(System.out::println,
                "src/test/resources/postgres-set1",
                postgres.listStatementParsers());
        System.out.println(sql);
    }

}
