import { describe, it, expect, vi } from "vitest";
import { configureStore } from "@reduxjs/toolkit";
import gameReducer, {
  startGame,
  endGame,
  increaseCorrectAnswers,
  setCurrentIndex,
  setTotalQuestions,
  updateScore,
} from "./gameSlice";
import playerReducer from "./playerSlice";
import { getEnvConfigValue } from "../../utils/envConfig";

// Mock služby pro backend
vi.mock("../../services/playerService", () => ({
  updatePlayerScore: vi.fn(async (_playerId: number, newScore: number) => ({
    score: newScore,
  })),
}));

function makeStore() {
  return configureStore({
    reducer: { game: gameReducer, player: playerReducer },
  });
}

describe("gameSlice reducers", () => {
  it("Akce startGame nastaví výchozí stav hry", () => {
    const store = makeStore();
    store.dispatch(startGame());

    const s = store.getState().game;
    expect(s.isPlaying).toBe(true);
    expect(s.score).toBe(getEnvConfigValue("VITE_INITIAL_SCORE", 200));
    expect(s.currentIndex).toBe(0);
    expect(s.correctAnswers).toBe(0);
    expect(s.totalQuestions).toBe(
      getEnvConfigValue("VITE_GAME_QUESTIONS_LIMIT", 20),
    );
  });

  it("Akce increaseCorrectAnswers zvýší počet správných odpovědí", () => {
    const store = makeStore();
    store.dispatch(increaseCorrectAnswers());
    expect(store.getState().game.correctAnswers).toBe(1);
  });

  it("Akce setCurrentIndex nastaví index aktuální otázky", () => {
    const store = makeStore();
    store.dispatch(setCurrentIndex(3));
    expect(store.getState().game.currentIndex).toBe(3);
  });

  it("Akce setTotalQuestions nastaví celkový počet otázek", () => {
    const store = makeStore();
    store.dispatch(setTotalQuestions(10));
    expect(store.getState().game.totalQuestions).toBe(10);
  });

  it("Akce endGame ukončí hru a resetuje počítadlo správných odpovědí", () => {
    const store = makeStore();
    store.dispatch(startGame());
    store.dispatch(increaseCorrectAnswers());
    store.dispatch(endGame());
    const s = store.getState().game;
    expect(s.isPlaying).toBe(false);
    expect(s.correctAnswers).toBe(0);
  });
});

describe("gameSlice updateScore thunk", () => {
  it("po úspěchu nastaví skóre na hodnotu ze serveru", async () => {
    const store = makeStore();
    // Výchozí score 200; změníme o +25 -> očekáváme 225
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    await store.dispatch(updateScore({ playerId: 42, scoreChange: 25 }) as any);
    expect(store.getState().game.score).toBe(225);
  });
});
