-- 1. Add the new column to questions
ALTER TABLE questions
    ADD COLUMN phishing_category_id BIGINT;

-- 2. Add the Foreign Key constraint
ALTER TABLE questions
    ADD CONSTRAINT fk_questions_phishing_category
        FOREIGN KEY (phishing_category_id)
            REFERENCES phishing_categories (id)
            ON DELETE RESTRICT;

-- 3. Add index for category lookups
CREATE INDEX idx_questions_phishing_category_id
    ON questions(phishing_category_id);

-- 4. Drop the join table
DROP TABLE IF EXISTS question_to_categories;

-- 5. Make the column NOT NULL
ALTER TABLE questions
    ALTER COLUMN phishing_category_id SET NOT NULL;