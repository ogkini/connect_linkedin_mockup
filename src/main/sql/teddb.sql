SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema teddb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `teddb` DEFAULT CHARACTER SET utf8 ;
USE `teddb` ;

-- -----------------------------------------------------
-- Table `teddb`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teddb`.`Users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `birthdate` DATE NULL,
  `email` VARCHAR(60) NOT NULL,
  `password` VARCHAR(120) NOT NULL,
  `occupation` BIGINT NULL,
  `experience` BIGINT NULL,
  `education` BIGINT NULL,
  `skills` BIGINT NULL,
  `accomplishments` BIGINT NULL,
  `projects` BIGINT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Occupation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teddb`.`Occupation` (
  `occupation_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(80) NOT NULL,
  `organisation` VARCHAR(80) NOT NULL,
  PRIMARY KEY (`occupation_id`),
  INDEX `fk_Occupation_1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Occupation_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Experience`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teddb`.`Experience` (
  `experience_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(80) NOT NULL,
  `company` VARCHAR(80) NOT NULL,
  `start_time` DATE NULL,
  `end_time` DATE NULL,
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
CREATE TABLE IF NOT EXISTS `teddb`.`Education` (
  `education_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `title` VARCHAR(80) NOT NULL,
  `school` VARCHAR(80) NOT NULL,
  `start_year` INT UNSIGNED NULL,
  `end_year` INT UNSIGNED NULL,
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
-- Table `teddb`.`Accomplishments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teddb`.`Accomplishments` (
  `accomplishment_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `type` INT UNSIGNED NOT NULL,
  `name` VARCHAR(80) NOT NULL,
  `info` VARCHAR(80) NULL,
  PRIMARY KEY (`accomplishment_id`),
  INDEX `fk_Accomplishments_1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Accomplishments_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `teddb`.`Projects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teddb`.`Projects` (
  `project_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  `description` VARCHAR(200) NULL,
  PRIMARY KEY (`project_id`),
  INDEX `fk_Projects_1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Projects_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `teddb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
