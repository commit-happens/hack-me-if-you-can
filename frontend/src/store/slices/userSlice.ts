import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../../store";

interface UserState {
  nickname: string;
}

const initialState: UserState = {
  nickname: "",
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setNickname: (state, action: PayloadAction<string>) => {
      state.nickname = action.payload;
    },
  },
});

export const { setNickname } = userSlice.actions;

// Selektory
export const selectNickname = (state: RootState) => state.user;

export default userSlice.reducer;
