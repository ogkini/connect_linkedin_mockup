SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema teddb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `teddb` ;

-- -----------------------------------------------------
-- Schema teddb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `teddb` DEFAULT CHARACTER SET utf8 ;
USE `teddb` ;

-- -----------------------------------------------------
-- User with privileges
-- -----------------------------------------------------
#DROP USER IF EXISTS ‘ted_user’@‘localhost’;
CREATE USER IF NOT EXISTS 'ted_user'@'localhost' IDENTIFIED BY 'ted';
GRANT ALL PRIVILEGES ON * . * TO 'ted_user'@'localhost';
FLUSH PRIVILEGES;

-- -----------------------------------------------------
-- Table `teddb`.`Roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Roles` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Roles` (
  `role_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB;

INSERT INTO Roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO Roles (name) VALUES ('ROLE_USER');

-- -----------------------------------------------------
-- Table `teddb`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Users` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `role_id` BIGINT NOT NULL,
  `picture` VARCHAR(200) NULL,
  PRIMARY KEY (`user_id`),
  INDEX `fk_Users_1_idx` (`role_id` ASC),
  CONSTRAINT `fk_Users_1`
    FOREIGN KEY (`role_id`)
    REFERENCES `teddb`.`Roles` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO Users (firstname, lastname, email, password, picture, role_id)
VALUES ('admin', 'admin', 'admin@mail.com', '$2a$10$9kuCCkLnpqz2WFt2ycj7Nux3T5PhYBLuGBznW0PNdaA9VRBqgEJgS', NULL, 1);

-- -----------------------------------------------------
-- Table `teddb`.`Occupation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Occupation` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Occupation` (
  `occupation_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(80) NOT NULL,
  `company` VARCHAR(45) NOT NULL,
  INDEX `fk_Occupation_1_idx` (`user_id` ASC),
  PRIMARY KEY (`occupation_id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC),
  CONSTRAINT `fk_Occupation_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Experience`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Experience` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Experience` (
  `experience_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(80) NOT NULL,
  `company` VARCHAR(45) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  PRIMARY KEY (`experience_id`),
  INDEX `fk_Experience_1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Experience_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Education`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Education` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Education` (
  `education_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(80) NOT NULL,
  `school` VARCHAR(80) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  PRIMARY KEY (`education_id`),
  INDEX `fk_Education_1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Education_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Skills`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Skills` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Skills` (
  `skill_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `strength` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`skill_id`),
  INDEX `fk_Skills_1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Skills_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Relationships`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Relationships` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Relationships` (
  `relationship_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_one_id` BIGINT NOT NULL,
  `user_two_id` BIGINT NOT NULL,
  `status` INT NOT NULL,
  `seen` TINYINT(1) NOT NULL,
  PRIMARY KEY (`relationship_id`),
  INDEX `fk_Relationship_1_idx` (`user_one_id` ASC),
  INDEX `fk_Relationship_2_idx` (`user_two_id` ASC),
  CONSTRAINT `fk_Relationship_1`
    FOREIGN KEY (`user_one_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Relationship_2`
    FOREIGN KEY (`user_two_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Posts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Posts` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Posts` (
  `post_id` BIGINT NOT NULL AUTO_INCREMENT,
  `owner_id` BIGINT NOT NULL,
  `text` VARCHAR(500) NOT NULL,
  `created_time` DATETIME NOT NULL,
  PRIMARY KEY (`post_id`),
  INDEX `fk_Post_1_idx` (`owner_id` ASC),
  CONSTRAINT `fk_Post_1`
    FOREIGN KEY (`owner_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Likes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Likes` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Likes` (
  `like_id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`like_id`),
  INDEX `fk_Like_1_idx` (`post_id` ASC),
  INDEX `fk_Like_2_idx` (`user_id` ASC),
  CONSTRAINT `fk_Like_1`
    FOREIGN KEY (`post_id`)
    REFERENCES `teddb`.`Posts` (`post_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Like_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Comments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Comments` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Comments` (
  `comment_id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `text` VARCHAR(200) NOT NULL,
  `created_time` DATETIME NOT NULL,
  PRIMARY KEY (`comment_id`),
  INDEX `fk_Comments_1_idx` (`post_id` ASC),
  INDEX `fk_Comments_2_idx` (`user_id` ASC),
  CONSTRAINT `fk_Comments_1`
    FOREIGN KEY (`post_id`)
    REFERENCES `teddb`.`Posts` (`post_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comments_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Conversations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Conversations` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Conversations` (
  `conversation_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_one_id` BIGINT NOT NULL,
  `user_two_id` BIGINT NOT NULL,
  PRIMARY KEY (`conversation_id`),
  INDEX `fk_Conversations_1_idx` (`user_one_id` ASC),
  INDEX `fk_Conversations_2_idx` (`user_two_id` ASC),
  CONSTRAINT `fk_Conversations_1`
    FOREIGN KEY (`user_one_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Conversations_2`
    FOREIGN KEY (`user_two_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Messages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Messages` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Messages` (
  `message_id` BIGINT NOT NULL AUTO_INCREMENT,
  `conversation_id` BIGINT NOT NULL,
  `sender_id` BIGINT NOT NULL,
  `receiver_id` BIGINT NOT NULL,
  `text` VARCHAR(400) NOT NULL,
  `created_time` DATETIME NOT NULL,
  `seen` TINYINT(1) NOT NULL,
  `opened` TINYINT(1) NOT NULL,
  PRIMARY KEY (`message_id`),
  INDEX `fk_Messages_1_idx` (`sender_id` ASC),
  INDEX `fk_Messages_2_idx` (`receiver_id` ASC),
  INDEX `fk_Messages_3_idx` (`conversation_id` ASC),
  CONSTRAINT `fk_Messages_1`
    FOREIGN KEY (`sender_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Messages_2`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Messages_3`
    FOREIGN KEY (`conversation_id`)
    REFERENCES `teddb`.`Conversations` (`conversation_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Notifications`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `teddb`.`Notifications` ;

CREATE TABLE IF NOT EXISTS `teddb`.`Notifications` (
  `notification_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `from_id` BIGINT NOT NULL,
  `action` VARCHAR(20) NOT NULL,
  `post_id` BIGINT NOT NULL,
  `created_time` DATETIME NOT NULL,
  `seen` TINYINT(1) NOT NULL,
  PRIMARY KEY (`notification_id`),
  INDEX `fk_Notifications_1_idx` (`user_id` ASC),
  INDEX `fk_Notifications_2_idx` (`post_id` ASC),
  INDEX `fk_Notifications_3_idx` (`from_id` ASC),
  CONSTRAINT `fk_Notifications_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Notifications_2`
    FOREIGN KEY (`post_id`)
    REFERENCES `teddb`.`Posts` (`post_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Notifications_3`
    FOREIGN KEY (`from_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
