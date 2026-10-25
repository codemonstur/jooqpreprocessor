-- Currently being addressed in https://github.com/jOOQ/jOOQ/issues/7241
-- Issue was closed
-- Workaround: remove the FIRST clause

CREATE TABLE `example3` (
 `example_id` int(10) unsigned NOT NULL,
 PRIMARY KEY (`example_id`)
);

ALTER TABLE `example3` ADD COLUMN `field1` int(10) unsigned NOT NULL FIRST;
