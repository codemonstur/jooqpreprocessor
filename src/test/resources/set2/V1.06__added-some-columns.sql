
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE `wedding` ADD COLUMN `address` varchar(64) NOT NULL AFTER `longitude`, ADD COLUMN `city` varchar(64) NOT NULL AFTER `address`, ADD COLUMN `country` varchar(64) NULL AFTER `city`;
SET FOREIGN_KEY_CHECKS = 1;
