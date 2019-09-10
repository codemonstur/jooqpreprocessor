-- Currently being addressed in https://github.com/jOOQ/jOOQ/issues/8846
-- Workaround: separate into individual ALTER statements

CREATE TABLE `example4` (
 `example_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `field1` int(11) unsigned NOT NULL,
 PRIMARY KEY (`example_id`)
);

ALTER TABLE `example4`
ADD COLUMN `field2` int(10) unsigned NOT NULL,
ADD COLUMN `field3` int(10) unsigned NOT NULL;
