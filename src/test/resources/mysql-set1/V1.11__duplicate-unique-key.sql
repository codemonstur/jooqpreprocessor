CREATE TABLE `table1` (
 `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 `email` varchar(45) NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE KEY `email_UNIQUE` (`email`)
);

CREATE TABLE `table2` (
 `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
 `email` varchar(45) NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE KEY `email_UNIQUE` (`email`)
);
