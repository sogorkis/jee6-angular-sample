--liquibase formatted SQL

--changeset stanislo:1

CREATE TABLE `members` (
  `id`           BIGINT(20)   NOT NULL,
  `email`        VARCHAR(255) NOT NULL,
  `name`         VARCHAR(25)  NOT NULL,
  `phone_number` VARCHAR(12)  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
)
  ENGINE =InnoDB
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

CREATE TABLE `crawl_requests` (
  `id`              BIGINT(20)   NOT NULL,
  `url`             VARCHAR(255) NOT NULL,
  `start_time`      TIMESTAMP    NOT NULL,
  `processing_time` INT          NULL,
  `return_code`     INT          NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  CHARACTER SET utf8
  COLLATE utf8_general_ci;
