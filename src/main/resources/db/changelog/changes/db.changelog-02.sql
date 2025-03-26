-- liquibase formatted sql
--  changeset Rokas:2
--  comment: Create question table.
CREATE TABLE questions (
    question_id UUID PRIMARY KEY NOT NULL,
    quiz_id UUID NOT NULL,
    image_id UUID,
    title VARCHAR(255) NOT NULL,
    CONSTRAINT fk_questions_quizzes FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id)
);