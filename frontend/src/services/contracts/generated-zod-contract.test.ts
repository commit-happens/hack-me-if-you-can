import { describe, expect, it } from "vitest";
import { submitAnswerBody } from "../generated-zod/answer-controller/answer-controller";
import {
  addPlayerBody,
  getPlayerSummaryQueryParams,
} from "../generated-zod/player-controller/player-controller";
import { getRandomQuestionsByDifficultyQueryParams } from "../generated-zod/question-controller/question-controller";

describe("generated Zod contract", () => {
  it("validuje addPlayerBody", () => {
    expect(() => addPlayerBody.parse({ nickname: "abc" })).not.toThrow();
    expect(() => addPlayerBody.parse({ nickname: "ab" })).toThrow();
  });

  it("validuje submitAnswerBody", () => {
    expect(() =>
      submitAnswerBody.parse({
        player_id: 1,
        question_id: 2,
        session_id: "session",
        is_phishing: true,
        remain_time: 60,
      }),
    ).not.toThrow();

    expect(() =>
      submitAnswerBody.parse({
        player_id: 1,
        question_id: 2,
        session_id: "session",
        is_phishing: true,
        remain_time: 61,
      }),
    ).toThrow();
  });

  it("validuje query parametry pro questions/random", () => {
    expect(() =>
      getRandomQuestionsByDifficultyQueryParams.parse({
        difficulty: "EASY",
        limit: 10,
      }),
    ).not.toThrow();

    expect(() =>
      getRandomQuestionsByDifficultyQueryParams.parse({
        difficulty: "INVALID",
        limit: 10,
      }),
    ).toThrow();
  });

  it("validuje optional session_id u player summary", () => {
    expect(() => getPlayerSummaryQueryParams.parse({})).not.toThrow();
    expect(() => getPlayerSummaryQueryParams.parse({ session_id: "abc" })).not.toThrow();
  });
});
