import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../../store";

interface GameState {
  score: number;
  correctAnswers: number;
  wrongAnswers: number;
  level: number;
  isPlaying: boolean;
}

const initialState: GameState = {
  score: 0,
  correctAnswers: 0,
  wrongAnswers: 0,
  level: 1,
  isPlaying: false,
};

const gameSlice = createSlice({
  name: "game",
  initialState,
  reducers: {
    startGame: (state) => {
      state.isPlaying = true;
      state.score = 100;
      state.level = 1;
    },
    endGame: (state) => {
      state.isPlaying = false;
    },
    incrementScore: (state, action: PayloadAction<number>) => {
      state.score += action.payload;
    },
    setLevel: (state, action: PayloadAction<number>) => {
      state.level = action.payload;
    },
  },
});

export const { startGame, endGame, incrementScore, setLevel } =
  gameSlice.actions;

// Selektory
export const selectScore = (state: RootState) => state.game.score;
export const selectLevel = (state: RootState) => state.game.level;
export const selectIsPlaying = (state: RootState) => state.game.isPlaying;
export const selectGameState = (state: RootState) => state.game;

export default gameSlice.reducer;
