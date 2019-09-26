## JOOQ preprocessor

Almost functional.

The jOOQ code generator is missing various features that prevents its use in a fully automated way.
The standard workaround is to maintain two lists of SQL scripts; one for the DB to do the migration and one for the code generator to work on.

Alternatively you can try rewriting your migration scripts to be parsable by both.
However there are some features that are really hard to get around because of limitations in the parser.

This preprocessor seeks to make development easier by taking the migration scripts and converting them to what the parser can handle.
The parser can then operate on the reduced set of instructions.

Its been a bit of journey to find all the quirks.
And the code has rough edges and is lacking tests.
But it is getting very close to doing its job.