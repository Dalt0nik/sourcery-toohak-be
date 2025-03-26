-- liquibase formatted sql
--  changeset Rokas:1
--  comment: Create quiz table.
CREATE TABLE quizzes
(
    quiz_id     UUID            PRIMARY KEY NOT NULL,
    created_by  UUID                             NOT NULL,
    title       VARCHAR(255)                     NOT NULL,
    description VARCHAR(255)                     NOT NULL
);