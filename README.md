
[![GitHub Release](https://img.shields.io/github/release/codemonstur/jooqpreprocessor.svg)](https://github.com/codemonstur/jooqpreprocessor/releases) 
[![Maven Central](https://img.shields.io/maven-central/v/com.github.codemonstur/jooqpreprocessor-maven-plugin.svg)](https://central.sonatype.com/artifact/com.github.codemonstur/jooqpreprocessor-maven-plugin)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)

# JOOQ preprocessor

Have been using this in production for some years now.
It works great.
The tool itself is highly opinionated though, it has been tuned to DB schema's that use the features I like. YMMV.

The jOOQ code generator is missing various features that prevents its use in a fully automated way.
The standard workaround is to maintain two lists of SQL scripts; one for the DB to do the migration and one for the code generator to work on.

Alternatively you can try rewriting your migration scripts to be parsable by both.
However there are some features that are really hard to get around because of limitations in the parser.

This preprocessor seeks to make development easier by taking the migration scripts and converting them to what the parser can handle.
The parser can then operate on the reduced set of instructions.

The project is a multi-module build:

- **`jooqpreprocessor-core`** — the SQL preprocessing engine and the jOOQ code generator every front-end shares.
- **`jooqpreprocessor-maven-plugin`** — a Maven goal that writes the preprocessed schema file, for jOOQ's own plugin to generate from.
- **`jooqpreprocessor-bobplugin`** — a [bob](https://github.com/codemonstur/bobthebuildtool) command (`jooq-codegen`) that preprocesses and generates the code in one step.
- **`jooqpreprocessor-cli`** — a standalone command line tool (bundled uber-jar, built with [jcli](https://github.com/codemonstur/jcli)).

## Maven

The goal binds to `generate-resources` and writes a single, jOOQ-parsable schema file from the migration scripts:

```xml
<plugin>
    <groupId>com.github.codemonstur</groupId>
    <artifactId>jooqpreprocessor-maven-plugin</artifactId>
    <version>3.0.0</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Configuration: `migrationSqlDir` (default `src/main/resources/db/migration`), `generationSqlFile`
(default `target/generated-resources/db/schema.sql`), `dbType` (`mysql` or `postgres`) and
`enabled` (default `true`).

## Bob

The bob plugin preprocesses the migrations and then runs the jOOQ code generator itself, in one command:

```
bob jooq-codegen
```

It is configured with the same flags as the command line tool, and only runs when the project
actually has migration scripts in the default location.

## Command line

Build the shaded jar with `mvn package`, producing `jooqpreprocessor-cli/target/jooqpreprocessor-cli-<version>.jar`.
It preprocesses the migration scripts and then generates the jOOQ sources, without Maven or bob:

```bash
java -jar jooqpreprocessor-cli.jar \
    -i src/main/resources/db/migration \
    --pre-process-file target/schema.sql \
    -o target/generated-sources/jooq \
    -n com.example.schema
```

Options:

| Option | Description |
| --- | --- |
| `-i`, `--input-scripts` | Directory containing the SQL migration scripts (default `src/main/resources/db/migration`). |
| `--pre-process-file` | File the preprocessed SQL is written to (default `instructions/schema.sql`). |
| `--db-type` | Dialect of the migration scripts: `mysql` or `postgres` (default `mysql`). |
| `-d`, `--dont-pre-process` | Skip preprocessing and give the input scripts to jOOQ as they are. |
| `-o`, `--output-dir` | Directory the generated jOOQ sources are written to (default `generate-sources/jooq`). |
| `-n`, `--package-name` | Package the generated sources are placed in (default `schema`). |
| `-l`, `--logging` | jOOQ logging level: `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR` or `FATAL` (default `WARN`). |
| `-h`, `--help` | Print usage. |

The jOOQ generator flags (`--wj` javadoc, `--wd` DAOs, `--wp` POJOs, `--wr` records, ...) are
listed by `--help`.

## Missing stuff

- Cannot handle semicolons in column comments
