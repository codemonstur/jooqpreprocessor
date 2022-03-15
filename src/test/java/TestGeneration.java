import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffAlgorithmListener;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import jooqpreprocessor.MavenGenerateJooqSql;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.jooq.meta.jaxb.Logging.FATAL;

public class TestGeneration {

    private static final String
        MIGRATION_SQL_DIR = "src/test/resources/test1",
        SQL_SCRIPT_PREPROCESSOR = "target/generated-resources/db/test1.sql",
        SQL_SCRIPT_MANUAL = "src/test/resources/test1.sql",
        DIRECTORY_JOOQ_CODE_PREPROCESSOR = "target/generated-sources/test1-pre",
        DIRECTORY_JOOQ_CODE_MANUAL = "target/generated-sources/test1-man";

    public static void main(final String... args) throws Exception {
        newJooqPreprocessor(MIGRATION_SQL_DIR, SQL_SCRIPT_PREPROCESSOR).execute();

        // This shows that there are differences, the manual sql also removed the UNIQUE keys and
        // the fields are in different orders. The first could be added but doesn't have to, the second
        // can't be fixed. So I think the code is good enough.
        comparePaths( generateJooqCode(SQL_SCRIPT_PREPROCESSOR, DIRECTORY_JOOQ_CODE_PREPROCESSOR)
                    , generateJooqCode(SQL_SCRIPT_MANUAL, DIRECTORY_JOOQ_CODE_MANUAL)
                    );
    }

    private static MavenGenerateJooqSql newJooqPreprocessor(final String inputDir, final String outputScript) {
        final MavenGenerateJooqSql gen = new MavenGenerateJooqSql();
        gen.enabled = true;
        gen.migrationSqlDir = inputDir;
        gen.generationSqlFile = outputScript;
        return gen;
    }

    private static Path generateJooqCode(final String inputScript, final String outputDir) throws Exception {
        GenerationTool.generate(newJooqConfiguration(inputScript, outputDir));
        return Paths.get(outputDir);
    }

    private static void comparePaths(final Path source, final Path target) throws IOException {
        Files.walk(source).filter(Files::isRegularFile).forEach(path -> compareFiles(path, toMatchingPath(source, path, target)));
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

    private static Path toMatchingPath(final Path rootDir, final Path file, final Path targetDir) {
        return Paths.get(targetDir.toString(), rootDir.relativize(file).toString());
    }

    private static void compareFiles(final Path file1, final Path file2) {
        System.out.println("========================================================================================");
        if (!file2.toFile().exists()) {
            System.out.println("File at " + file1 + " has no matching file in target dir");
            return;
        }
        try {
            final Patch<String> diff = DiffUtils.diff(new String(Files.readAllBytes(file1), UTF_8)
                    , new String(Files.readAllBytes(file2), UTF_8), newNopDiffAlgorithmListener());

            final List<AbstractDelta<String>> deltas = diff.getDeltas();
            if (!deltas.isEmpty()) {
                System.out.println("=== There are differences between " + file1 + " and " + file2);
                for (final AbstractDelta<String> delta : deltas) {
                    System.out.println("=== Change of type: " + delta.getType());
                    System.out.println("=== Source is:\n" + delta.getSource().toString());
                    System.out.println("=== Target is:\n" + delta.getTarget().toString());
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static DiffAlgorithmListener newNopDiffAlgorithmListener() {
        return new DiffAlgorithmListener() {
            public void diffStart() {}
            public void diffStep(int value, int max) {}
            public void diffEnd() {}
        };
    }

}
