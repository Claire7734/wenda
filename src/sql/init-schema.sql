#CREATE DATABASE wenda;
USE wenda;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`user_id` INT(6) UNSIGNED NOT NULL AUTO_INCREMENT,
`name` VARCHAR(64) NOT NULL,
`password` VARCHAR(120) NOT NULL,
`salt` VARCHAR(32) NOT NULL,
`head_url` VARCHAR(256) NOT NULL,
PRIMARY KEY (user_id),
UNIQUE KEY `name`(`name`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
`question_id` INT NOT NULL AUTO_INCREMENT,
`title` VARCHAR(255) NOT NULL,
`content` TEXT NULL,
`user_id` INT NOT NULL,
`created_date` DATETIME NOT NULL,
`comment_count` INT NOT NULL DEFAULT 0,
PRIMARY KEY (`question_id`),
INDEX `date_index` (`created_date` ASC)
)ENGINE=INNODB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket` (
  `ticket_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `ticket` VARCHAR(45) NOT NULL,
  `expired` DATETIME NOT NULL,
  `status` INT NULL DEFAULT 0,
  PRIMARY KEY (`ticket_id`),
  UNIQUE INDEX `ticket_UNIQUE` (`ticket` ASC)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `comment_id` INT NOT NULL AUTO_INCREMENT,
  `content` TEXT NULL,
  `user_id` INT NOT NULL,
  `created_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `entity_id` INT NOT NULL,
  `entity_type` INT NOT NULL ,
  `status` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`comment_id`),
  INDEX `entity_index` (`entity_id` ASC, `entity_type` ASC),
  INDEX `user_index` (`user_id` ASC),
  INDEX `date_index` (`created_date` ASC)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

