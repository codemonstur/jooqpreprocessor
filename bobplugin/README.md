# jooqpreprocessor-bobplugin

The bob plugin: a `jooq-codegen` command that preprocesses the migrations and
then generates the jOOQ code, in one step.

It registers `jooq-codegen` with [bob](https://github.com/codemonstur/bobthebuildtool),
which preprocesses `src/main/resources/db/migration` into the schema file and runs
the jOOQ code generator over it, adding the generated sources to the project. A
project without migration scripts in that location is left alone, so installing
the plugin never breaks a build that does not use it.

## Build

```bash
mvn -pl jooqpreprocessor-bobplugin -am clean package
```

The shaded jar is written to `target/jooqpreprocessor-bobplugin.jar`.

## Usage

```
bob jooq-codegen
```

The command takes the same flags as the [command line tool](../jooqpreprocessor-cli/README.md)
(`--db-type`, `--wj`, `--wd`, `--wp`, ...), resolved relative to the project root.

See the [root README](../README.md) for the whole project.
