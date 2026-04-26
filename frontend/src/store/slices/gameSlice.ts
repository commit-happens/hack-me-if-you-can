import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../../store";
import { getEnvConfigValue } from "../../utils/envConfig";

const gameQuestionsLimitDefault = getEnvConfigValue("VITE_GAME_QUESTIONS_LIMIT", 20);
const gameInitialScoreDefault = 100;

function createGameSessionId() {
  if (typeof crypto !== "undefined" && typeof crypto.randomUUID === "function") {
    return crypto.randomUUID();
  }

  return `session-${Date.now()}-${Math.random().toString(36).slice(2, 10)}`;
}

interface GameState {
  score: number;
  correctAnswers: number;
  currentIndex: number;
  isPlaying: boolean;
  totalQuestions: number;
  sessionId: string;
}

const initialState: GameState = {
  score: gameInitialScoreDefault,
  correctAnswers: 0,
  currentIndex: 0,
  isPlaying: false,
  totalQuestions: gameQuestionsLimitDefault,
  sessionId: "",
};

const gameSlice = createSlice({
  name: "game",
  initialState,
  reducers: {
    startGame: (state) => {
      state.isPlaying = true;
      state.score = gameInitialScoreDefault;
      state.currentIndex = 0;
      state.correctAnswers = 0;
      state.totalQuestions = gameQuestionsLimitDefault;
      state.sessionId = createGameSessionId();
    },
    endGame: (state) => {
      state.isPlaying = false;
      state.correctAnswers = 0;
    },
    increaseCorrectAnswers: (state) => {
      state.correctAnswers += 1;
    },
    setCurrentIndex: (state, action: PayloadAction<number>) => {
      state.currentIndex = action.payload;
    },
    setTotalQuestions: (state, action: PayloadAction<number>) => {
      state.totalQuestions = action.payload;
    },
    setScore: (state, action: PayloadAction<number>) => {
      state.score = action.payload;
    },
  },
});

export const {
  startGame,
  endGame,
  setCurrentIndex,
  increaseCorrectAnswers,
  setTotalQuestions,
  setScore,
} = gameSlice.actions;

// Selektory
export const selectScore = (state: RootState) => state.game.score;
export const selectCurrentIndex = (state: RootState) => state.game.currentIndex;
export const selectIsPlaying = (state: RootState) => state.game.isPlaying;
export const selectSessionId = (state: RootState) => state.game.sessionId;
export const selectGameState = (state: RootState) => state.game;

export default gameSlice.reducer;
