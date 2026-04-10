ALTER TABLE phishing_categories
    ADD COLUMN reward_points INT;

UPDATE phishing_categories
SET reward_points = CASE tag
    WHEN 'LEGIT' THEN 100
    WHEN 'LOTTERY' THEN 250
    WHEN 'URGENT' THEN 500
    WHEN 'FAKE_URL' THEN 600
    WHEN 'FAKE_DOC' THEN 700
    WHEN 'CRED_THEFT' THEN 900
    WHEN 'SPEAR_PHISH' THEN 1000
    ELSE 0
END;

ALTER TABLE phishing_categories
    ALTER COLUMN reward_points SET NOT NULL;

CREATE TABLE answers (
    player_id BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    question_id BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    session_id VARCHAR(64) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    earned_points INT NOT NULL,
    difficulty_points INT NOT NULL,
    categories_points INT NOT NULL,
    speed_bonus INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (player_id, question_id, session_id)
);

CREATE INDEX idx_answers_player_id ON answers(player_id);
CREATE INDEX idx_answers_question_id ON answers(question_id);
CREATE INDEX idx_answers_player_session_created_at ON answers(player_id, session_id, created_at DESC);


