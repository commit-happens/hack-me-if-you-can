ALTER TABLE questions
    ADD COLUMN problems JSONB NOT NULL DEFAULT '[]'::jsonb;
