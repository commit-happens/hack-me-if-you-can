import { beforeEach, describe, expect, it, vi } from "vitest";

vi.mock("../httpClient", () => ({
  httpClient: vi.fn(),
}));

import { httpClient } from "../httpClient";
import { submitAnswer } from "../generated/answer-controller/answer-controller";
import { hello } from "../generated/hello-controller/hello-controller";
import {
  addPlayer,
  getPlayer,
  getPlayerSummary,
  getPlayers,
} from "../generated/player-controller/player-controller";
import { getRandomQuestionsByDifficulty } from "../generated/question-controller/question-controller";

const httpClientMock = vi.mocked(httpClient);

describe("generated API controllers contract", () => {
  beforeEach(() => {
    httpClientMock.mockReset();
    httpClientMock.mockResolvedValue(undefined as never);
  });

  it("hello vola GET /hello", async () => {
    await hello();

    expect(httpClientMock).toHaveBeenCalledWith(
      { url: "/hello", method: "GET", signal: undefined },
      undefined,
    );
  });

  it("question controller vola GET /questions/random s query parametry", async () => {
    await getRandomQuestionsByDifficulty({ difficulty: "EASY", limit: 3 });

    expect(httpClientMock).toHaveBeenCalledWith(
      {
        url: "/questions/random",
        method: "GET",
        params: { difficulty: "EASY", limit: 3 },
        signal: undefined,
      },
      undefined,
    );
  });

  it("answer controller vola POST /answers s JSON body", async () => {
    const body = {
      player_id: 1,
      question_id: 10,
      session_id: "abc",
      is_phishing: false,
      remain_time: 30,
    };

    await submitAnswer(body);

    expect(httpClientMock).toHaveBeenCalledWith(
      {
        url: "/answers",
        method: "POST",
        headers: { "Content-Type": "application/json" },
        data: body,
      },
      undefined,
    );
  });

  it("player controller mapuje vsechny endpointy", async () => {
    await getPlayers();
    await addPlayer({ nickname: "Tester" });
    await getPlayer(15);
    await getPlayerSummary(15, { session_id: "session-1" });

    expect(httpClientMock).toHaveBeenNthCalledWith(
      1,
      { url: "/players", method: "GET", signal: undefined },
      undefined,
    );
    expect(httpClientMock).toHaveBeenNthCalledWith(
      2,
      {
        url: "/players",
        method: "POST",
        headers: { "Content-Type": "application/json" },
        data: { nickname: "Tester" },
      },
      undefined,
    );
    expect(httpClientMock).toHaveBeenNthCalledWith(
      3,
      { url: "/players/15", method: "GET", signal: undefined },
      undefined,
    );
    expect(httpClientMock).toHaveBeenNthCalledWith(
      4,
      {
        url: "/players/15",
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        data: { score: 42 },
      },
      undefined,
    );
    expect(httpClientMock).toHaveBeenNthCalledWith(
      5,
      {
        url: "/players/15/summary",
        method: "GET",
        params: { session_id: "session-1" },
        signal: undefined,
      },
      undefined,
    );
  });
});
