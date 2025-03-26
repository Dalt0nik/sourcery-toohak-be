-- liquibase formatted sql

-- changeset JSF2025S-9:001
-- comment: Failų saugykla. URL saugomas kaip pilnas kelias iki failo, reiškias privalo pradžia būti nuo root "/"
CREATE TABLE files
(
    id                UUID PRIMARY KEY,
    file_type         VARCHAR(100) NOT NULL CHECK (file_type IN ('image/png', 'image/jpeg')),
    file_url          TEXT         NOT NULL,
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- changeset JSF2025S-9:002
-- comment: Jei prireiks daugiau provider negu tik auth0, tai galima pridėti.
CREATE TABLE external_providers
(
    id            UUID PRIMARY KEY,
    provider_name VARCHAR(200) UNIQUE NOT NULL
);

-- changeset JSF2025S-9:003
-- comment: Paskyros profile. UUID nes reikės per endpoints viešai rodyti, todėl nenorime naudoti INTEGER
CREATE TABLE app_users
(
    id                   UUID PRIMARY KEY,
    picture              UUID REFERENCES files (id),
    external_provider_id UUID                NOT NULL REFERENCES external_providers (id),
    email                VARCHAR(200) UNIQUE NOT NULL,
    username             VARCHAR(200) UNIQUE NOT NULL,
    created_at           TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP
);

-- changeset JSF2025S-9:004
-- comment: Aukščiausias piramides taškas
CREATE TABLE quizzes
(
    id          UUID PRIMARY KEY,
    created_by  UUID         NOT NULL REFERENCES app_users (id),
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- changeset JSF2025S-9:005
-- comment: Žemesnis taškas už quizzes. Užduotas klausimas
CREATE TABLE questions
(
    id      UUID PRIMARY KEY,
    quiz_id UUID NOT NULL REFERENCES quizzes (id),
    image   UUID REFERENCES files (id),
    title   VARCHAR(200) NOT NULL
);


-- changeset JSF2025S-9:006
-- comment: Žemiausias quizzes taškas. Klausimo apibrėžimas. Eiliškumas nusakomas su ordering
CREATE TABLE question_options
(
    id             UUID PRIMARY KEY,
    question_id    UUID         NOT NULL REFERENCES questions (id),
    title          VARCHAR(200) NOT NULL,
    ordering       INTEGER      NOT NULL,
    is_correct     BOOLEAN      NOT NULL DEFAULT FALSE,
    UNIQUE (question_id, ordering)
);

-- changeset JSF2025S-9:007
-- comment: PENDING - laukiama prisijungiančių žaidėju
-- comment: ACTIVE - vyksta klausimynas
-- comment: INACTIVE - baigėsi klausimynas
CREATE TYPE quiz_status AS ENUM ('ACTIVE', 'INACTIVE', 'PENDING');

-- changeset JSF2025S-9:008
-- comment: sesija į kurią bando žaidėjai jungtis
CREATE TABLE quiz_sessions
(
    id          UUID PRIMARY KEY,
    status      quiz_status NOT NULL,
    created_at  TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,
    last_active TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP
);

-- changeset JSF2025S-9:009
-- comment: Anonimiški žaidėjai, kurie jungiasi iš savo telefonų
CREATE TABLE quiz_players
(
    id              UUID PRIMARY KEY,
    quiz_id         UUID         NOT NULL REFERENCES quizzes (id),
    quiz_session_id UUID         NOT NULL REFERENCES quiz_sessions (id),
    nickname        VARCHAR(100) NOT NULL,
    score           INTEGER      NOT NULL DEFAULT 0,
    joined_at       TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP
);

-- changeset JSF2025S-9:010
-- comment: Žaidėju atsakymų sekimas. Tikriname kad vienas žaidėjas gali tik vieną kart į tą patį klausimą atsakyti.
CREATE TABLE player_answers
(
    id             UUID PRIMARY KEY,
    quiz_player_id UUID    NOT NULL REFERENCES quiz_players (id),
    question_id    UUID    NOT NULL REFERENCES questions (id),
    option_id      UUID    NOT NULL REFERENCES question_options (id),
    answered_at    TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (quiz_player_id, question_id)
);

-- changeset JSF2025S-9:011
CREATE UNIQUE INDEX uq_question_correct_option
    ON question_options (question_id)
    WHERE is_correct = TRUE;
-- changeset JSF2025S-9:012
CREATE INDEX idx_questions_quiz_id ON questions (quiz_id);
-- changeset JSF2025S-9:013
CREATE INDEX idx_question_options_question_id ON question_options (question_id);
-- changeset JSF2025S-9:014
CREATE INDEX idx_quiz_players_quiz_id ON quiz_players (quiz_id);
-- changeset JSF2025S-9:015
CREATE INDEX idx_quiz_players_quiz_session_id ON quiz_players (quiz_session_id);
-- changeset JSF2025S-9:016
CREATE INDEX idx_player_answers_quiz_player_id ON player_answers (quiz_player_id);
-- changeset JSF2025S-9:017
CREATE INDEX idx_player_answers_question_id ON player_answers (question_id);