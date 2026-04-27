const API_BASE_URL = "http://localhost:8080";

export interface PlayerModel {
  playerId: number;
  nickname: string;
  score: number;
}

export const getPlayers = async (): Promise<PlayerModel[]> => {
  const response = await fetch(`${API_BASE_URL}/players`);
  if (!response.ok) {
    throw new Error(`Chyba při načítání hráčů: ${response.statusText}`);
  }
  const result = await response.json();

  return result;
};

export const createPlayer = async (nickname: string): Promise<PlayerModel> => {
  const response = await fetch(`${API_BASE_URL}/players`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      nickname,
    }),
  });

  if (!response.ok) {
    const error = await response.json();
    // Pokud má backend strukturovanou chybu s fields, použijeme ji
    if (error.fields?.nickname) {
      throw new Error(error.fields.nickname);
    }
    throw new Error(
      error.error ||
        error.message ||
        `Chyba při vytváření hráče: ${response.statusText}`,
    );
  }

  return response.json();
};

export const updatePlayerScore = async (
  playerId: number,
  score: number,
): Promise<PlayerModel> => {
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
      error.error ||
        error.message ||
        `Chyba při aktualizaci skóre hráče: ${response.statusText}`,
    );
  }

  return response.json();
};
