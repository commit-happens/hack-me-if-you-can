import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../../store";

interface GameState {
  score: number;
  correctAnswers: number;
  currentIndex: number;
  isPlaying: boolean;
}

const initialState: GameState = {
  score: 100,
  correctAnswers: 0,
  currentIndex: 0,
  isPlaying: false,
};

const gameSlice = createSlice({
  name: "game",
  initialState,
  reducers: {
    startGame: (state) => {
      state.isPlaying = true;
      state.score = 100;
      state.currentIndex = 0;
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
    setCurrentIndex: (state, action: PayloadAction<number>) => {
      state.currentIndex = action.payload;
    },
  },
});

export const { startGame, endGame, incrementScore, setCurrentIndex, increaseCorrectAnswers } =
  gameSlice.actions;

// Selektory
export const selectScore = (state: RootState) => state.game.score;
export const selectCurrentIndex = (state: RootState) => state.game.currentIndex;
export const selectIsPlaying = (state: RootState) => state.game.isPlaying;
export const selectGameState = (state: RootState) => state.game;

export default gameSlice.reducer;
