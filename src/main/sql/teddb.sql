SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema teddb
-- -----------------------------------------------------
#DROP SCHEMA IF EXISTS `teddb` ;
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
#DROP TABLE IF EXISTS `teddb`.`Roles` ;
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
#DROP TABLE IF EXISTS `teddb`.`Users` ;
CREATE TABLE IF NOT EXISTS `teddb`.`Users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `picture` VARCHAR(45) NULL,
  `role_id` BIGINT NOT NULL,
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
