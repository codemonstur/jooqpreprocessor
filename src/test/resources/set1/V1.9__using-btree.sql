-- Currently being addressed in https://github.com/jOOQ/jOOQ/issues/9132
-- Workaround: remove the USING BTREE clause

CREATE TABLE `example9` (
 `example_id` int(10) unsigned NOT NULL
);

ALTER TABLE `example9` ADD PRIMARY KEY (`example_id`) USING BTREE;