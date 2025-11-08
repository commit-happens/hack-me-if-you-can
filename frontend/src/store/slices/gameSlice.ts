import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../../store";

interface GameState {
  score: number;
  correctAnswers: number;
  wrongAnswers: number;
  order: number;
  isPlaying: boolean;
}

const initialState: GameState = {
  score: 0,
  correctAnswers: 0,
  wrongAnswers: 0,
  order: 1,
  isPlaying: false,
};

const gameSlice = createSlice({
  name: "game",
  initialState,
  reducers: {
    startGame: (state) => {
      state.isPlaying = true;
      state.score = 100;
      state.order = 1;
    },
    endGame: (state) => {
      state.isPlaying = false;
    },
    incrementScore: (state, action: PayloadAction<number>) => {
      state.score += action.payload;
    },
    increaseCorrectAnswers: (state) => {
      state.correctAnswers += 1;
    },
    setOrder: (state, action: PayloadAction<number>) => {
      state.order = action.payload;
    },
  },
});

export const { startGame, endGame, incrementScore, setOrder, increaseCorrectAnswers } =
  gameSlice.actions;

// Selektory
export const selectScore = (state: RootState) => state.game.score;
export const selectOrder = (state: RootState) => state.game.order;
export const selectIsPlaying = (state: RootState) => state.game.isPlaying;
export const selectGameState = (state: RootState) => state.game;

export default gameSlice.reducer;
