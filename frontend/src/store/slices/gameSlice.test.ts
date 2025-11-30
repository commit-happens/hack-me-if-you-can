import { describe, it, expect, vi } from "vitest";
import { configureStore } from "@reduxjs/toolkit";
import gameReducer, {
  startGame,
  endGame,
  increaseCorrectAnswers,
  setCurrentIndex,
  updateScore,
} from "./gameSlice";
import playerReducer from "./playerSlice";

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
  it("startGame nastaví výchozí stav hry", () => {
    const store = makeStore();
    store.dispatch(startGame());
    const s = store.getState().game;
    expect(s.isPlaying).toBe(true);
    expect(s.score).toBe(100);
    expect(s.currentIndex).toBe(0);
    expect(s.correctAnswers).toBe(0);
  });

  it("increaseCorrectAnswers zvýší počet správných odpovědí", () => {
    const store = makeStore();
    store.dispatch(increaseCorrectAnswers());
    expect(store.getState().game.correctAnswers).toBe(1);
  });

  it("setCurrentIndex nastaví index aktuální otázky", () => {
    const store = makeStore();
    store.dispatch(setCurrentIndex(3));
    expect(store.getState().game.currentIndex).toBe(3);
  });

  it("endGame ukončí hru a resetuje počítadlo správných odpovědí", () => {
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
    // Výchozí score 100; změníme o +25 -> očekáváme 125
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    await store.dispatch(updateScore({ playerId: 42, scoreChange: 25 }) as any);
    expect(store.getState().game.score).toBe(125);
  });
});
