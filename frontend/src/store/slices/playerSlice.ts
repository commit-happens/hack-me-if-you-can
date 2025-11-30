import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "..";

interface PlayerState {
  nickname: string;
  playerId: number | null;
}

const initialState: PlayerState = {
  nickname: "",
  playerId: null,
};

const playerSlice = createSlice({
  name: "player",
  initialState,
  reducers: {
    setPlayer: (
      state,
      action: PayloadAction<{ nickname: string; playerId: number }>,
    ) => {
      state.nickname = action.payload.nickname;
      state.playerId = action.payload.playerId;
    },
  },
});

export const { setPlayer } = playerSlice.actions;

// Selektory
export const selectNickname = (state: RootState) => state.player.nickname;
export const selectPlayerId = (state: RootState) => state.player.playerId;

export default playerSlice.reducer;
