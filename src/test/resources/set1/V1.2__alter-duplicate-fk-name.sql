-- Currently being addressed in https://github.com/jOOQ/jOOQ/issues/9130
-- Workaround: remove the second name in the statement

CREATE TABLE `example2` (
 `example_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 PRIMARY KEY (`example_id`)
);

CREATE TABLE `example2_link` (
 `link_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `example_id` int(10) unsigned NOT NULL,
 PRIMARY KEY (`link_id`)
);

ALTER TABLE `example2_link`
ADD CONSTRAINT `fk_example2_link` FOREIGN KEY `fk_example2_link` (`example_id`) REFERENCES `example2` (`example_id`) ON DELETE CASCADE ON UPDATE CASCADE;
