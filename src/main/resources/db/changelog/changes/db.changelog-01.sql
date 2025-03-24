-- liquibase formatted sql
--  changeset Rokas:1
--  comment: Create quiz table.
CREATE TABLE quiz
(
    quiz_id     BIGSERIAL            PRIMARY KEY NOT NULL,
    title       VARCHAR(255)                      NOT NULL,
    description VARCHAR(255)                      NOT NULL
);