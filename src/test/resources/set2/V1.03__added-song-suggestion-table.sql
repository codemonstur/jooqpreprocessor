
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `song_suggestion` ( `suggestion_id` int(10) unsigned NOT NULL AUTO_INCREMENT, `wedding_id` int(10) unsigned NOT NULL, `artist` varchar(64) COLLATE utf8mb4_bin NOT NULL, `title` varchar(64) COLLATE utf8mb4_bin NOT NULL, `suggester` varchar(64) COLLATE utf8mb4_bin NOT NULL, `timestamp` bigint(20) NOT NULL, PRIMARY KEY (`suggestion_id`), KEY `fk_suggestion_wedding_idx` (`wedding_id`), CONSTRAINT `fk_suggestion_wedding` FOREIGN KEY (`wedding_id`) REFERENCES `wedding` (`wedding_id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
SET FOREIGN_KEY_CHECKS = 1;
