import { describe, it, expect } from "vitest";
import { configureStore } from "@reduxjs/toolkit";
import gameReducer, {
  startGame,
  endGame,
  increaseCorrectAnswers,
  setCurrentIndex,
  setScore,
  setTotalQuestions,
} from "./gameSlice";
import playerReducer from "./playerSlice";
import { getEnvConfigValue } from "../../utils/envConfig";

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
    expect(s.score).toBe(getEnvConfigValue("VITE_INITIAL_SCORE", 100));
    expect(s.currentIndex).toBe(0);
    expect(s.correctAnswers).toBe(0);
    expect(s.totalQuestions).toBe(getEnvConfigValue("VITE_GAME_QUESTIONS_LIMIT", 20));
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

describe("gameSlice setScore reducer", () => {
  it("nastaví skóre na zadanou hodnotu", () => {
    const store = makeStore();
    // Výchozí score 100; správná odpověď přidá 25 bodů -> očekáváme 125
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    await store.dispatch(updateScore({ playerId: 42, scoreChange: 25 }) as any);
    expect(store.getState().game.score).toBe(125);
  });
});
