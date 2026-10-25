-- Currently being addressed in https://github.com/jOOQ/jOOQ/issues/9132
-- Issue was closed
-- Workaround:
--
-- CREATE TABLE `example` (
--  `example_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
--  `index` int(10) unsigned NOT NULL,
--  PRIMARY KEY (`example_id`)
-- );
--
-- CREATE INDEX `index_idx` ON example (`index`);

CREATE TABLE `example6` (
 `example_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `index` int(10) unsigned NOT NULL,
 PRIMARY KEY (`example_id`),
 KEY `index_idx` (`index`)
);
