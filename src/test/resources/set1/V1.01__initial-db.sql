
SET FOREIGN_KEY_CHECKS = 0;
SET NAMES utf8mb4;

CREATE TABLE `audio` (
  `audio_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `museum_id` int(10) unsigned NOT NULL,
  `file` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `category` varchar(45) NOT NULL,
  `created_date` bigint(20) NOT NULL,
  PRIMARY KEY (`audio_id`),
  KEY `fk_ma_museum_idx` (`museum_id`),
  KEY `fk_mu_museum_idx` (`museum_id`),
  CONSTRAINT `fk_mu_museum` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`museum_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `item` (
  `item_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tour_id` int(10) unsigned NOT NULL,
  `screen_type` int(10) unsigned DEFAULT '0',
  `thumbnail` int(10) unsigned DEFAULT NULL,
  `title` varchar(45) NOT NULL,
  `subtitle` varchar(45) DEFAULT NULL,
  `text` text NOT NULL,
  `museum_code` varchar(45) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `audio` int(11) unsigned DEFAULT NULL,
  `order` int(11) unsigned NOT NULL DEFAULT '99',
  `published` bit(1) DEFAULT b'0',
  PRIMARY KEY (`item_id`),
  KEY `fk_item_tour_idx` (`tour_id`),
  KEY `fk_item_audio_idx` (`audio`),
  KEY `fk_item_media_idx` (`thumbnail`),
  CONSTRAINT `fk_item_audio` FOREIGN KEY (`audio`) REFERENCES `audio` (`audio_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_item_media` FOREIGN KEY (`thumbnail`) REFERENCES `media` (`media_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_item_tour` FOREIGN KEY (`tour_id`) REFERENCES `tour` (`tour_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `item_media` (
  `item_id` int(10) unsigned NOT NULL,
  `order` int(10) unsigned NOT NULL DEFAULT '99',
  `media_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`item_id`,`media_id`),
  KEY `fk_item_media_item_idx` (`item_id`),
  KEY `fk_item_media_image_idx` (`media_id`),
  CONSTRAINT `fk_item_media_item` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_item_media_media` FOREIGN KEY (`media_id`) REFERENCES `media` (`media_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `link_submission_question` (
  `submission_id` int(10) unsigned NOT NULL,
  `question_id` int(10) unsigned NOT NULL,
  `answer` int(11) NOT NULL,
  PRIMARY KEY (`submission_id`,`question_id`),
  KEY `fk_lsq_question_idx` (`question_id`),
  CONSTRAINT `fk_lsq_question` FOREIGN KEY (`question_id`) REFERENCES `school_question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_lsq_submission` FOREIGN KEY (`submission_id`) REFERENCES `school_test_submission` (`submission_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `link_test_question` (
  `test_id` int(10) unsigned NOT NULL,
  `question_id` int(10) unsigned NOT NULL,
  `order` int(10) unsigned NOT NULL,
  PRIMARY KEY (`test_id`,`question_id`),
  KEY `fk_ltq_question_idx` (`question_id`),
  CONSTRAINT `fk_ltq_question` FOREIGN KEY (`question_id`) REFERENCES `school_question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ltq_test` FOREIGN KEY (`test_id`) REFERENCES `school_test` (`test_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `media` (
  `media_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `museum_id` int(10) unsigned NOT NULL,
  `file` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `category` varchar(45) NOT NULL,
  `created_date` bigint(20) NOT NULL,
  PRIMARY KEY (`media_id`),
  KEY `fk_mi_museum_idx` (`museum_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `museum` (
  `museum_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(64) NOT NULL,
  `salt` varchar(64) DEFAULT NULL,
  `stretching` bigint(20) NOT NULL,
  `algorithm` bigint(20) NOT NULL,
  `strategy` bigint(20) NOT NULL,
  `avatar` varchar(45) DEFAULT NULL,
  `museum_logo` varchar(45) DEFAULT NULL,
  `museum_name` varchar(45) DEFAULT NULL,
  `museum_description` text,
  `museum_latitude` double DEFAULT NULL,
  `museum_longitude` double DEFAULT NULL,
  `created_date` bigint(20) NOT NULL DEFAULT '0',
  `last_login` bigint(20) NOT NULL DEFAULT '0',
  `enabled` bit(1) NOT NULL DEFAULT b'1',
  `balance` int(11) NOT NULL DEFAULT '0',
  `last_deduction` bigint(20) NOT NULL DEFAULT '0',
  `free_account` bit(1) NOT NULL DEFAULT b'0',
  `visibility` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`museum_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `museum_added_info` (
  `info_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `museum_id` int(10) unsigned NOT NULL,
  `information` varchar(128) NOT NULL,
  PRIMARY KEY (`info_id`),
  KEY `fk_mai_museum_idx` (`museum_id`),
  CONSTRAINT `fk_mai_museum` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`museum_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `museum_address` (
  `museum_id` int(10) unsigned NOT NULL,
  `address_line` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `postal_code` varchar(45) NOT NULL,
  `country` varchar(45) NOT NULL,
  PRIMARY KEY (`museum_id`),
  CONSTRAINT `fk_ma_museum` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`museum_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `museum_feedback` (
  `feedback_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `museum_id` int(10) unsigned NOT NULL,
  `name` varchar(64) NOT NULL,
  `email` varchar(64) DEFAULT NULL,
  `phone_number` varchar(64) DEFAULT NULL,
  `text` text NOT NULL,
  `created_date` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`feedback_id`),
  KEY `fk_feedback_museum_idx` (`museum_id`),
  CONSTRAINT `fk_feedback_museum` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`museum_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `museum_question` (
  `question_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `museum_id` int(10) unsigned NOT NULL,
  `question` varchar(256) NOT NULL,
  `answer_a` varchar(128) NOT NULL,
  `answer_b` varchar(128) NOT NULL,
  `answer_c` varchar(128) DEFAULT NULL,
  `answer_d` varchar(128) DEFAULT NULL,
  `answer_e` varchar(128) DEFAULT NULL,
  `correct` int(10) unsigned NOT NULL,
  `published` bit(1) NOT NULL DEFAULT b'0',
  `created_date` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`question_id`),
  KEY `fk_question_museum_idx` (`museum_id`),
  CONSTRAINT `fk_question_museum` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`museum_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `school` (
  `school_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(64) NOT NULL,
  `salt` varchar(64) DEFAULT NULL,
  `stretching` bigint(20) NOT NULL,
  `algorithm` bigint(20) NOT NULL,
  `strategy` bigint(20) NOT NULL,
  `avatar` varchar(45) DEFAULT NULL,
  `created_date` bigint(20) NOT NULL DEFAULT '0',
  `last_login` bigint(20) NOT NULL DEFAULT '0',
  `enabled` bit(1) NOT NULL DEFAULT b'1',
  `school_name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`school_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `school_question` (
  `question_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `school_id` int(10) unsigned NOT NULL,
  `museum_id` int(10) unsigned NOT NULL,
  `original_question` int(10) unsigned DEFAULT NULL,
  `question` varchar(256) NOT NULL,
  `answer_a` varchar(128) NOT NULL,
  `answer_b` varchar(128) NOT NULL,
  `answer_c` varchar(128) DEFAULT NULL,
  `answer_d` varchar(128) DEFAULT NULL,
  `answer_e` varchar(128) DEFAULT NULL,
  `correct` int(11) NOT NULL,
  `created_date` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`question_id`),
  KEY `fk_school_question_school_idx` (`school_id`),
  KEY `fk_sq_question_idx` (`original_question`),
  KEY `fk_sq_museum_idx` (`museum_id`),
  CONSTRAINT `fk_sq_museum` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`museum_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sq_question` FOREIGN KEY (`original_question`) REFERENCES `museum_question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sq_school` FOREIGN KEY (`school_id`) REFERENCES `school` (`school_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `school_test` (
  `test_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `school_id` int(10) unsigned NOT NULL,
  `museum_id` int(10) unsigned NOT NULL,
  `name` varchar(45) NOT NULL,
  `created_date` bigint(20) NOT NULL,
  PRIMARY KEY (`test_id`),
  KEY `fk_test_school_idx` (`school_id`),
  KEY `fk_test_museum_idx` (`museum_id`),
  CONSTRAINT `fk_test_museum` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`museum_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_test_school` FOREIGN KEY (`school_id`) REFERENCES `school` (`school_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `school_test_submission` (
  `submission_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `test_id` int(10) unsigned NOT NULL,
  `name` varchar(64) NOT NULL,
  `created_date` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`submission_id`),
  KEY `fk_submission_test_idx` (`test_id`),
  CONSTRAINT `fk_submission_test` FOREIGN KEY (`test_id`) REFERENCES `school_test` (`test_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `tour` (
  `tour_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `subtitle` varchar(45) DEFAULT NULL,
  `description` text,
  `thumbnail` int(10) unsigned DEFAULT NULL,
  `language` varchar(45) NOT NULL,
  `museum_id` int(10) unsigned NOT NULL,
  `order` int(11) unsigned NOT NULL DEFAULT '99',
  `published` bit(1) NOT NULL DEFAULT b'0',
  `created_date` bigint(20) NOT NULL DEFAULT '0',
  `published_date` bigint(20) NOT NULL DEFAULT '0',
  `modified_date` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`tour_id`),
  KEY `fk_tour_account_idx` (`museum_id`),
  KEY `fk_tour_media_idx` (`thumbnail`),
  CONSTRAINT `fk_tour_media` FOREIGN KEY (`thumbnail`) REFERENCES `media` (`media_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_tour_museum` FOREIGN KEY (`museum_id`) REFERENCES `museum` (`museum_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
set FOREIGN_KEY_CHECKS = 1;
