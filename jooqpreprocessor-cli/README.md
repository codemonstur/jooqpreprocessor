# jooqpreprocessor-cli

The standalone command line tool of jooqpreprocessor, built as an uber-jar with
[jcli](https://github.com/codemonstur/jcli).

It preprocesses the migration scripts into jOOQ-parsable SQL and then runs the
jOOQ code generator over them, without Maven or bob.

## Build

```bash
mvn -pl jooqpreprocessor-cli -am clean package
```

The shaded jar is written to `target/jooqpreprocessor-cli-<version>.jar`.

## Usage

```bash
java -jar jooqpreprocessor-cli.jar \
    -i src/main/resources/db/migration \
    --pre-process-file target/schema.sql \
    -o target/generated-sources/jooq \
    -n com.example.schema
```

Relative paths are resolved against the working directory. See the
[root README](../README.md) for the full option list, or run with `--help`.
