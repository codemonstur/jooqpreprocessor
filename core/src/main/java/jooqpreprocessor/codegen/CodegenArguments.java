package jooqpreprocessor.codegen;

import jcli.annotations.CliCommand;
import jcli.annotations.CliOption;
import jooqpreprocessor.model.DbType;
import org.jooq.meta.jaxb.Logging;

/**
 * The options of the jOOQ code generator, shared by the bob plugin and the command line tool.
 * <p>
 * The class only carries values: how a relative path is resolved (against the bob project or
 * against the working directory) is decided by the caller.
 */
@CliCommand(name = "jooqpreprocessor", description =
    "Preprocesses SQL migration scripts into jOOQ-parsable SQL and generates jOOQ code.")
public final class CodegenArguments {

    /** Default location of the migration scripts, relative to the project root. */
    public static final String DEFAULT_INPUT_LOCATION = "src/main/resources/db/migration";

    /** Directory holding the SQL migration scripts. */
    @CliOption(name = 'i', longName = "input-scripts", defaultValue = DEFAULT_INPUT_LOCATION,
        description = "Directory containing the SQL migration scripts")
    public String inputScript;

    /** Directory the generated jOOQ sources are written to. */
    @CliOption(name = 'o', longName = "output-dir", defaultValue = "generate-sources/jooq",
        description = "Directory the generated jOOQ sources are written to")
    public String outputDir;

    /** Generate javadoc on the generated code. */
    @CliOption(longName = "wj", description = "Generate javadoc for the generated code")
    public boolean withJavadoc;

    /** Carry comments from the parsed SQL into the generated code. */
    @CliOption(longName = "wc", description = "Carry comments from the SQL into the generated code")
    public boolean withComments;

    /** Skip generation of empty schemas. */
    @CliOption(longName = "wes", description = "Do not generate empty schemas")
    public boolean withEmptySchemas;

    /** Skip generation of empty catalogs. */
    @CliOption(longName = "wec", description = "Do not generate empty catalogs")
    public boolean withEmptyCatalogs;

    /** Generate the {@code @Generated} annotation. */
    @CliOption(longName = "wga", description = "Annotate generated code with @Generated")
    public boolean withGeneratedAnnotation;

    /** Generate global catalog references. */
    @CliOption(longName = "wgcr", description = "Generate global catalog references")
    public boolean withGlobalCatalogReferences;

    /** Generate DAOs. */
    @CliOption(longName = "wd", description = "Generate DAOs")
    public boolean withDaos;

    /** Generate POJOs. */
    @CliOption(longName = "wp", description = "Generate POJOs")
    public boolean withPojos;

    /** Generate index classes. */
    @CliOption(longName = "wi", description = "Generate indexes")
    public boolean withIndexes;

    /** Generate key classes. */
    @CliOption(longName = "wk", description = "Generate keys")
    public boolean withKeys;

    /** Generate record classes. */
    @CliOption(longName = "wr", description = "Generate records")
    public boolean withRecords;

    /** Use instance fields instead of getters and setters. */
    @CliOption(longName = "wif", description = "Use instance fields instead of getters and setters")
    public boolean withInstanceFields;

    /** The jOOQ logging level. */
    @CliOption(name = 'l', longName = "logging", defaultValue = "WARN",
        description = "jOOQ logging level: TRACE, DEBUG, INFO, WARN, ERROR or FATAL")
    public Logging logging;

    /** Package the generated sources are placed in. When empty a package name is derived by the caller. */
    @CliOption(name = 'n', longName = "package-name",
        description = "Package the generated sources are placed in")
    public String packageName;

    /** Skip preprocessing and hand the input scripts to jOOQ untouched. */
    @CliOption(name = 'd', longName = "dont-pre-process",
        description = "Skip preprocessing and give the input scripts to jOOQ as they are")
    public boolean dontPreProcess;

    /** File the preprocessed (jOOQ-parsable) SQL is written to, before code generation runs. */
    @CliOption(longName = "pre-process-file", defaultValue = "instructions/schema.sql",
        description = "File the preprocessed SQL is written to")
    public String generationSqlFile;

    /** Dialect the migration scripts are written in. */
    @CliOption(longName = "db-type", defaultValue = "mysql",
        description = "Dialect of the migration scripts: mysql or postgres")
    public DbType dbType;

    /** Print the help text and exit. */
    @CliOption(name = 'h', longName = "help", isHelp = true,
        description = "Print this help text and exit")
    public boolean help;

}
