-- Currently being addressed in https://github.com/jOOQ/jOOQ/issues/8846
-- and https://github.com/jOOQ/jOOQ/issues/8834

-- This is correct code according to MySQL but it fails in JOOQ
-- JOOQ doesn't allow multiple clauses in ALTER statements, if you use the standard workaround and turn it into
-- two clauses like so:
--
-- ALTER TABLE `example` DROP COLUMN `example`;
-- ALTER TABLE `example` DROP PRIMARY KEY;
--
-- Then JOOQ will also complain that there is no primary key to delete, which makes sense since you just
-- deleted the relevant field. The clauses in the ALTER statement are executed from left to right by MySQL
-- but somehow the error in the right statement gets ignored, and the query executes correctly. Odd.
--

CREATE TABLE `example8` (
 `example` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `field1` int(10) unsigned NOT NULL,
 PRIMARY KEY (`example`)
);

ALTER TABLE `example8`
  DROP COLUMN `example`,
  DROP PRIMARY KEY;