ALTER TABLE questions
    ADD CONSTRAINT check_questions_difficulty
    CHECK (difficulty IN (1, 2, 3));

