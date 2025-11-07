import { configureStore } from '@reduxjs/toolkit';
import gameReducer from './slices/gameSlice';
import mainReducer from './slices/mainSlice';

export const store = configureStore({
  reducer: {
    main: mainReducer,
    game: gameReducer,
  },
});

// Typy pro TypeScript
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
