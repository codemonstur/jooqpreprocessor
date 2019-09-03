import jooqpreprocessor.MavenGenerateJooqSql;
import org.apache.maven.plugin.MojoFailureException;

public class TestGeneration {

    public static void main(final String... args) throws MojoFailureException {
        final MavenGenerateJooqSql gen = new MavenGenerateJooqSql();
        gen.enabled = true;
        gen.migrationSqlDir = "src/test/resources/set2";
        gen.generationSqlFile = "target/generated-resources/db/fulldb.sql";
        gen.execute();
    }

}
