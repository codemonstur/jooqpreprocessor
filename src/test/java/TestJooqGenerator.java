import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.jooq.meta.jaxb.Logging.FATAL;

public class TestJooqGenerator {

    public static void main(final String... args) throws Exception {
        generateJooqCode("src/test/resources/set1/V1.12__alter-unique-index.sql", "target/generate-sources");
    }

    private static Path generateJooqCode(final String inputScript, final String outputDir) throws Exception {
        GenerationTool.generate(newJooqConfiguration(inputScript, outputDir));
        return Paths.get(outputDir);
    }

    private static Configuration newJooqConfiguration(final String scriptToParse, final String outputDirectory) {
        return new Configuration()
            .withLogging(FATAL)
            .withGenerator(new Generator()
                .withGenerate(new Generate()
                    .withJavadoc(false)
                    .withComments(false)
                    .withEmptySchemas(false)
                    .withEmptyCatalogs(false)
                    .withGeneratedAnnotation(false)
                    .withGlobalCatalogReferences(false)
                    .withDaos(false)
                    .withPojos(false)
                    .withIndexes(false)
                    .withKeys(false)
                    .withRecords(false))
                .withDatabase(new Database()
                    .withName("org.jooq.meta.extensions.ddl.DDLDatabase")
                    .withProperties(new Property()
                        .withKey("scripts")
                        .withValue(scriptToParse)))
                .withTarget(new Target()
                    .withPackageName("codegen")
                    .withDirectory(outputDirectory)));
    }

}
