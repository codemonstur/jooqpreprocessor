# jooqpreprocessor-maven-plugin

The Maven plugin: rewrites the migration scripts into jOOQ-parsable SQL.

The `generate` goal binds to `generate-resources` and writes a single schema file
that the jOOQ code generator can parse. It only preprocesses — generating the
sources is left to jOOQ's own plugin, which keeps this plugin free of jOOQ.

## Build

```bash
mvn -pl jooqpreprocessor-maven-plugin -am clean package
```

## Usage

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

| Parameter | Default | Description |
| --- | --- | --- |
| `enabled` | `true` | Whether the goal runs at all. |
| `migrationSqlDir` | `src/main/resources/db/migration` | Directory holding the SQL migration scripts. |
| `generationSqlFile` | `target/generated-resources/db/schema.sql` | File the preprocessed SQL is written to. |
| `dbType` | `mysql` | Dialect of the migration scripts: `mysql` or `postgres`. |

See the [root README](../README.md) for the whole project.
