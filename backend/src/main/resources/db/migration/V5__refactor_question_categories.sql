ALTER TABLE questions
    ADD COLUMN phishing_category_id BIGINT;

ALTER TABLE questions
    ADD CONSTRAINT fk_questions_phishing_category
        FOREIGN KEY (phishing_category_id)
            REFERENCES phishing_categories (id)
            ON DELETE RESTRICT;

CREATE INDEX idx_questions_phishing_category_id
    ON questions(phishing_category_id);

DROP TABLE IF EXISTS question_to_categories;

ALTER TABLE questions
    ALTER COLUMN phishing_category_id SET NOT NULL;
