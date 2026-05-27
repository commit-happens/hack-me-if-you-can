import { combineReducers, configureStore } from "@reduxjs/toolkit";
import gameReducer from "./slices/gameSlice";
import playerReducer from "./slices/playerSlice";

const rootReducer = combineReducers({
  game: gameReducer,
  player: playerReducer,
});

function createAppStore() {
  return configureStore({
    reducer: rootReducer,
  });
}

type AppStore = ReturnType<typeof createAppStore>;

declare global {
  var __HMIYC_REDUX_STORE__: AppStore | undefined;
}

function getStore() {
  if (import.meta.env.DEV) {
    if (!globalThis.__HMIYC_REDUX_STORE__) {
      globalThis.__HMIYC_REDUX_STORE__ = createAppStore();
    }

    return globalThis.__HMIYC_REDUX_STORE__;
  }

  return createAppStore();
}

export const store = getStore();

// Typy pro TypeScript
export type RootState = ReturnType<typeof rootReducer>;
export type AppDispatch = typeof store.dispatch;
