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
  `picture` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`user_id`),
  INDEX `fk_Users_1_idx` (`role_id` ASC),
  CONSTRAINT `fk_Users_1`
    FOREIGN KEY (`role_id`)
    REFERENCES `teddb`.`Roles` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO Users (firstname, lastname, email, password, picture, role_id)
VALUES ('admin', 'admin', 'admin@mail.com', '$2a$10$9kuCCkLnpqz2WFt2ycj7Nux3T5PhYBLuGBznW0PNdaA9VRBqgEJgS', 'generic.png', 1);

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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
