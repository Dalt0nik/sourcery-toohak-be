-- liquibase formatted sql

-- changeset JSF2025S-61:005
ALTER TABLE quizzes
    ADD COLUMN cover_image UUID;

-- changeset JSF2025S-61:006
ALTER TABLE quizzes
    ADD CONSTRAINT fk_cover_image
        FOREIGN KEY (cover_image) REFERENCES files (id);