
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `gift_promise` ( `promise_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `gift_id` int(10) unsigned NOT NULL, `timestamp` bigint(20) NOT NULL, `name` varchar(64) COLLATE utf8mb4_bin NOT NULL, PRIMARY KEY (`promise_id`), KEY `fk_promise_gift_idx` (`gift_id`), CONSTRAINT `fk_promise_gift` FOREIGN KEY (`gift_id`) REFERENCES `gift` (`gift_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
ALTER TABLE `invitation` ADD COLUMN `wedding_id` int(10) unsigned NOT NULL AFTER `invitation_id`, ADD INDEX `fk_invitation_wedding_idx` (`wedding_id`) USING BTREE, ADD CONSTRAINT `fk_invitation_wedding` FOREIGN KEY `fk_invitation_wedding` (`wedding_id`) REFERENCES `wedding` (`wedding_id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `reminder` ADD COLUMN `reminder_id` int(10) unsigned NOT NULL auto_increment FIRST, DROP COLUMN `reminder`, DROP PRIMARY KEY, ADD PRIMARY KEY (`reminder_id`) USING BTREE;
SET FOREIGN_KEY_CHECKS = 1;
