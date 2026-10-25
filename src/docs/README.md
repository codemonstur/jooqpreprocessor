# jooqpreprocessor

A preprocessor that converts database migration scripts into the parser-safe SQL
form that [jOOQ](https://www.jooq.org/) codegen requires. Available as a Maven
plugin, a bob plugin and a command line tool — all sharing one core.

## The problem

jOOQ's SQL parser cannot handle several constructs that commonly appear in
migration scripts (certain `ALTER TABLE` forms, index/charset clauses, foreign-key
check toggles, and so on). The usual workaround is to maintain **two parallel sets
of scripts**: real migrations for the database, and a stripped-down copy for
jOOQ — which inevitably drifts apart.

This project removes that duplication: it reads your migrations once, rewrites them
into a single schema file that jOOQ's parser accepts, and (except in the Maven
plugin) runs codegen against that file.

> The approach is **highly opinionated**: it assumes a conventional migration
> layout and naming scheme. Known limitation: semicolons inside column comments
> are not handled.

## The modules

| Module | What it is |
| --- | --- |
| `jooqpreprocessor-core` | The preprocessing engine and the jOOQ code generator. A library. |
| `jooqpreprocessor-maven-plugin` | The `generate` goal: writes the schema file for jOOQ's own plugin. |
| `jooqpreprocessor-bobplugin` | The `jooq-codegen` bob command: preprocesses and generates in one step. |
| `jooqpreprocessor-cli` | A standalone command line tool (uber-jar). |

## Usage

### Maven

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

The goal binds to `generate-resources`.

| Parameter | Default | Description |
| --- | --- | --- |
| `enabled` | `true` | Master switch. |
| `migrationSqlDir` | `src/main/resources/db/migration` | Input directory of migration scripts. |
| `generationSqlFile` | `target/generated-resources/db/schema.sql` | Merged, parser-safe output file. |
| `dbType` | `mysql` | Dialect: `mysql` or `postgres`. |

### Bob

```
bob jooq-codegen
```

### Command line

```bash
java -jar jooqpreprocessor-cli.jar \
    -i src/main/resources/db/migration \
    --pre-process-file target/schema.sql \
    -o target/generated-sources/jooq \
    -n com.example.schema
```

See the [root README](../../README.md) for the full option list.

## How it works

`MigrationsProcessor`:

1. Lists migration files, sorted by the version in the filename (`V1__init.sql`
   as well as `V1.10__after.sql`, ordered the way Flyway orders them).
2. Strips `--` line comments and `/* ... */` block comments.
3. Splits statements on `;`.
4. Applies every matching `StatementParser` to rewrite unsupported constructs.
5. Concatenates the result into the schema file.

The bob plugin and the command line tool then hand that file to `JooqCodegen`,
which runs the jOOQ generator over it with the `DDLDatabase`.

## Supported statement parsers

| MySQL | PostgreSQL |
| --- | --- |
| `MyCreateTable` | `PgCreateTable` |
| `MyAlterTable` | `PgAlterTable` |
| `MyDropTable` | `PgSet` |
| `MyCreateIndex` | `PgCreateSchema` |
| `MyForeignKeyChecks` | |
| `MyNameUTF8` | |

## Relationship to bobplugin-jooqcodegen

The [bobplugin-jooqcodegen](../../bobplugin-jooqcodegen/) project used to carry
its own copy of the preprocessing idea (jOOQ 3.17.x). Its parser improvements now
live in `jooqpreprocessor-core`, and the bob plugin module of this project replaces
it (jOOQ 3.18.x). The original project is left in place, untouched.

## Building

```sh
make build
make test
```

## License

MIT
