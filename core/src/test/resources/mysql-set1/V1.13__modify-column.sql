CREATE TABLE `example` (
 `id` int(10) unsigned NOT NULL,
 `email` int(10) unsigned NOT NULL,
 PRIMARY KEY (`id`)
);

ALTER TABLE `example` MODIFY COLUMN `email` varchar(64) NULL DEFAULT NULL;