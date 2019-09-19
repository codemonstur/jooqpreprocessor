CREATE TABLE `example` (
 `id` int(10) unsigned NOT NULL,
 `email` varchar(64) NOT NULL,
 `org_id` int(10) unsigned NOT NULL,
 PRIMARY KEY (`id`)
);

-- This query fails on the unique index part, just like V1.12. But I wonder if it also fails on the USING BTREE part?
ALTER TABLE `example` ADD UNIQUE INDEX `unique_example` (`email`, `org_id`) USING BTREE;