# jooqpreprocessor-core

The SQL preprocessing engine and the jOOQ code generator every front-end shares.

This module contains the statement parsers (MySQL and PostgreSQL), the migration
reader that merges the scripts in order and rewrites what jOOQ cannot parse, and
the jOOQ code generator configuration. The Maven plugin, the bob plugin and the
command line tool all run through it. Other tools can depend on it directly:

```xml
<dependency>
    <groupId>com.github.codemonstur</groupId>
    <artifactId>jooqpreprocessor-core</artifactId>
    <version>3.0.0</version>
</dependency>
```

jOOQ itself (`jooq-codegen`, `jooq-meta-extensions`) and `jcli` are *optional*
dependencies here: the code generator is only available to the modules that also
declare them, which keeps the Maven plugin — that merely preprocesses — free of
jOOQ at build time.

## Build

```bash
mvn -pl jooqpreprocessor-core clean install
```

## Entry points

- `jooqpreprocessor.MigrationsProcessor` — `processSQL` merges and rewrites the
  migration scripts, `writeSchemaFile` writes the result.
- `jooqpreprocessor.codegen.JooqCodegen` — `preprocess` and `generate` drive the
  whole pipeline for the command line tool and the bob plugin.
- `jooqpreprocessor.model.DbType` — the dialect, which selects the statement parsers.

See the [root README](../README.md) for the Maven, bob and command line usage.
