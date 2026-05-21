CREATE TABLE problems (
    id          BIGSERIAL PRIMARY KEY,
    tag         VARCHAR(100) NOT NULL UNIQUE,
    description TEXT        NOT NULL
);

CREATE TABLE question_problems (
    question_id BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    problem_id  BIGINT NOT NULL REFERENCES problems(id)  ON DELETE CASCADE,
    PRIMARY KEY (question_id, problem_id)
);
