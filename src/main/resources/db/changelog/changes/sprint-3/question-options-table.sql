-- liquibase formatted sql

-- changeset JSF2025S-66:002
-- comment: Add ON DELETE CASCADE to question_options.question_id foreign key
ALTER TABLE question_options
DROP CONSTRAINT question_options_question_id_fkey;

ALTER TABLE question_options
ADD CONSTRAINT question_options_question_id_fkey
FOREIGN KEY (question_id)
REFERENCES questions(id)
ON DELETE CASCADE;