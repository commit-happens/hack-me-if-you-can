const API_BASE_URL = "http://localhost:8080/api";

export interface PlayerRequest {
  nickname: string;
  score: number;
}

export interface PlayerModel {
  playerId: number;
  nickname: string;
  score: number;
}

export const getPlayers = async (): Promise<PlayerModel[]> => {
  try {
    const response = await fetch(`${API_BASE_URL}/players`);
    if (!response.ok) {
      throw new Error(`Failed to fetch players: ${response.statusText}`);
    }
    const result = await response.json();

    return result;
  } catch (error) {
    throw error;
  }
};

export const createPlayer = async (nickname: string): Promise<PlayerModel> => {
  const response = await fetch(`${API_BASE_URL}/players`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      nickname,
      score: 100, // Automatické skóre
    }),
  });

  if (!response.ok) {
    const error = await response.json();
    // Pokud má backend strukturovanou chybu s fields, použijeme ji
    if (error.fields?.nickname) {
      throw new Error(error.fields.nickname);
    }
    throw new Error(
      error.error || error.message || `Failed to create player: ${response.statusText}`
    );
  }

  return response.json();
};

export const updatePlayerScore = async (playerId: number, score: number): Promise<PlayerModel> => {
  const response = await fetch(`${API_BASE_URL}/players/${playerId}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      score: score,
    }),
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(
      error.error || error.message || `Failed to update player score: ${response.statusText}`
    );
  }

  return response.json();
};
