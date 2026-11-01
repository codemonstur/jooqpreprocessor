CREATE TABLE `table` (
 `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 `email` varchar(45) NOT NULL,
 PRIMARY KEY (`id`)
);

ALTER TABLE `table` ADD UNIQUE INDEX `email_UNIQUE` (`email`);
