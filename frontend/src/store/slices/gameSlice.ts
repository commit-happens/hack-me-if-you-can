import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../../store";
import { updatePlayerScore } from "../../services/playerService";

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

// Async thunk pro update skóre na backendu
// Server-authoritativní update skóre: nejprve PATCH na backend, pak teprve upravíme lokální stav.
export const updateScore = createAsyncThunk(
  "game/updateScore",
  async (
    { playerId, scoreChange }: { playerId: number; scoreChange: number },
    { getState },
  ) => {
    const state = getState() as RootState;
    const currentScore = state.game.score;
    const targetScore = currentScore + scoreChange;
    console.log(
      `Posílám na backend nové skóre hráče ${playerId}: ${currentScore} + (${scoreChange}) => ${targetScore}`,
    );
    const updated = await updatePlayerScore(playerId, targetScore);
    return updated.score; // Vracíme absolutní skóre ze serveru
  },
);

const gameSlice = createSlice({
  name: "game",
  initialState,
  reducers: {
    startGame: (state) => {
      state.isPlaying = true;
      state.score = 100;
      state.currentIndex = 0;
      state.correctAnswers = 0;
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
  },
  extraReducers: (builder) => {
    builder.addCase(updateScore.fulfilled, (state, action) => {
      state.score = action.payload;
    });
    builder.addCase(updateScore.rejected, (_state, action) => {
      console.error(
        "Nepodařilo se aktualizovat skóre na backendu:",
        action.error,
      );
    });
  },
});

export const { startGame, endGame, setCurrentIndex, increaseCorrectAnswers } =
  gameSlice.actions;

// Selektory
export const selectScore = (state: RootState) => state.game.score;
export const selectCurrentIndex = (state: RootState) => state.game.currentIndex;
export const selectIsPlaying = (state: RootState) => state.game.isPlaying;
export const selectGameState = (state: RootState) => state.game;

export default gameSlice.reducer;
