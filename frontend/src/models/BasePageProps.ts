import type Page from "./Page";

// Player hook type definition
export interface PlayerHook {
  player: any;
  isLoading: boolean;
  error: string | null;
  createPlayer: (nickname: string) => Promise<void>;
  updateScore: (newScore: number) => Promise<void>;
  loadPlayer: (nickname: string) => Promise<void>;
  checkPlayerExists: (nickname: string) => Promise<boolean>;
  clearPlayer: () => void;
  clearError: () => void;
  hasPlayer: boolean;
  playerName: string;
  playerScore: number;
}

export type BasePageProps = {
  page?: Page;
  navigate?: (page: Page) => void;
  playerHook?: PlayerHook;
};
