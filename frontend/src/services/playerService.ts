/**
 * Player Service - API communication layer for player operations
 * 
 * This service handles all HTTP requests to the Spring Boot backend API.
 * It provides a clean interface between the React frontend and the REST API.
 * 
 * API ENDPOINTS:
 * - POST /api/players/score - Create or update player
 * - GET /api/players/{nickname}/score - Get player score
 * - GET /api/players/{nickname}/exists - Check if player exists
 * - DELETE /api/players/{nickname}/score - Delete player
 */

// Base URL for the backend API - matches Spring Boot server configuration
const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Interface for player score request (matches backend PlayerScoreRequest DTO)
 */
export interface PlayerScoreRequest {
  nickname: string;
  score: number;
}

/**
 * Interface for player score response (matches backend PlayerScoreResponse DTO)
 */
export interface PlayerScoreResponse {
  nickname: string;
  score: number;
  message?: string;
}

/**
 * Interface for player exists response
 */
export interface PlayerExistsResponse {
  exists: boolean;
}

/**
 * Player Service class with all API operations
 */
export class PlayerService {
  
  /**
   * Creates a new player with nickname and initial score of 0.
   * This is called when a player enters their nickname and starts the game.
   * 
   * @param nickname - Player's unique nickname
   * @returns Promise with player data or error
   */
  static async createPlayer(nickname: string): Promise<PlayerScoreResponse> {
    try {
      const response = await fetch(`${API_BASE_URL}/players/score`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nickname: nickname.trim(),
          score: 0 // New players start with 0 score
        } as PlayerScoreRequest)
      });

      if (!response.ok) {
        if (response.status === 400) {
          const errorData = await response.json();
          throw new Error(errorData.message || 'Invalid nickname format');
        }
        throw new Error(`Server error: ${response.status}`);
      }

      const data: PlayerScoreResponse = await response.json();
      return data;
    } catch (error) {
      console.error('Error creating player:', error);
      throw error;
    }
  }

  /**
   * Gets a player's current score by nickname.
   * 
   * @param nickname - Player's nickname
   * @returns Promise with player score data
   */
  static async getPlayerScore(nickname: string): Promise<PlayerScoreResponse> {
    try {
      const response = await fetch(`${API_BASE_URL}/players/${encodeURIComponent(nickname)}/score`);
      
      if (!response.ok) {
        if (response.status === 404) {
          throw new Error('Player not found');
        }
        throw new Error(`Server error: ${response.status}`);
      }

      const data: PlayerScoreResponse = await response.json();
      return data;
    } catch (error) {
      console.error('Error getting player score:', error);
      throw error;
    }
  }

  /**
   * Updates a player's score.
   * 
   * @param nickname - Player's nickname
   * @param score - New score value
   * @returns Promise with updated player data
   */
  static async updatePlayerScore(nickname: string, score: number): Promise<PlayerScoreResponse> {
    try {
      const response = await fetch(`${API_BASE_URL}/players/score`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nickname: nickname.trim(),
          score: score
        } as PlayerScoreRequest)
      });

      if (!response.ok) {
        throw new Error(`Server error: ${response.status}`);
      }

      const data: PlayerScoreResponse = await response.json();
      return data;
    } catch (error) {
      console.error('Error updating player score:', error);
      throw error;
    }
  }

  /**
   * Checks if a player with given nickname already exists.
   * 
   * @param nickname - Nickname to check
   * @returns Promise with boolean result
   */
  static async playerExists(nickname: string): Promise<boolean> {
    try {
      const response = await fetch(`${API_BASE_URL}/players/${encodeURIComponent(nickname)}/exists`);
      
      if (!response.ok) {
        throw new Error(`Server error: ${response.status}`);
      }

      const data: PlayerExistsResponse = await response.json();
      return data.exists;
    } catch (error) {
      console.error('Error checking player existence:', error);
      throw error;
    }
  }

  /**
   * Deletes a player from the system.
   * 
   * @param nickname - Nickname of player to delete
   * @returns Promise with success message
   */
  static async deletePlayer(nickname: string): Promise<string> {
    try {
      const response = await fetch(`${API_BASE_URL}/players/${encodeURIComponent(nickname)}/score`, {
        method: 'DELETE'
      });

      if (!response.ok) {
        if (response.status === 404) {
          throw new Error('Player not found');
        }
        throw new Error(`Server error: ${response.status}`);
      }

      const data = await response.json();
      return data.message || 'Player deleted successfully';
    } catch (error) {
      console.error('Error deleting player:', error);
      throw error;
    }
  }
}

export default PlayerService;