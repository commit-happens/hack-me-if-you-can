import { configureStore } from "@reduxjs/toolkit";
import gameReducer from "./slices/gameSlice";
import userReducer from "./slices/userSlice";

export const store = configureStore({
  reducer: {
    game: gameReducer,
    user: userReducer,
  },
});

// Typy pro TypeScript
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
