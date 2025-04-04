-- liquibase formatted sql

-- changeset JSF2025S-66:001
-- comment: Add ON DELETE CASCADE to questions.quiz_id foreign key
ALTER TABLE questions
DROP CONSTRAINT questions_quiz_id_fkey;

ALTER TABLE questions
ADD CONSTRAINT questions_quiz_id_fkey
FOREIGN KEY (quiz_id)
REFERENCES quizzes(id)
ON DELETE CASCADE;