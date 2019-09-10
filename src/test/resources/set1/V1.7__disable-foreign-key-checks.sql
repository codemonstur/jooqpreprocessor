-- Currently being addressed in https://github.com/jOOQ/jOOQ/issues/9084
-- Workaround: rearrange the queries

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `example7` (
 `example_id` int(10) unsigned NOT NULL,
 PRIMARY KEY (`example_id`),
 CONSTRAINT `fk_example_next` FOREIGN KEY (`example_id`) REFERENCES `next7` (`next_id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `next7` (
 `next_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 PRIMARY KEY (`next_id`)
);

set FOREIGN_KEY_CHECKS = 1;
