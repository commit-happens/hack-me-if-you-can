import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../../store";

interface UserState {
  nickname: string;
  playerId: number | null;
}

const initialState: UserState = {
  nickname: "",
  playerId: null,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setPlayer: (state, action: PayloadAction<{ nickname: string; playerId: number }>) => {
      state.nickname = action.payload.nickname;
      state.playerId = action.payload.playerId;
    },
  },
});

export const { setPlayer } = userSlice.actions;

// Selektory
export const selectNickname = (state: RootState) => state.user.nickname;
export const selectPlayerId = (state: RootState) => state.user.playerId;

export default userSlice.reducer;
