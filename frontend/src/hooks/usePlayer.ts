import { useState, useCallback } from "react";
import { PlayerService, type PlayerScoreResponse } from "../services/playerService";

/**
 * Custom hook for managing player state and API operations.
 * 
 * This hook provides a clean interface for React components to interact
 * with player data and handle loading states, errors, and success scenarios.
 * 
 * USAGE EXAMPLE:
 * ```tsx
 * const { player, isLoading, error, createPlayer, updateScore } = usePlayer();
 * 
 * const handleCreatePlayer = async (nickname: string) => {
 *   await createPlayer(nickname);
 * };
 * ```
 */
export const usePlayer = () => {
  // State management for player data
  const [player, setPlayer] = useState<PlayerScoreResponse | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  /**
   * Creates a new player and stores in state.
   * Called when user enters nickname and starts game.
   * 
   * @param nickname - Player's chosen nickname
   * @returns Promise that resolves when player is created
   */
  const createPlayer = useCallback(async (nickname: string): Promise<void> => {
    if (!nickname || nickname.trim().length < 3) {
      setError('Nickname must be at least 3 characters long');
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      const newPlayer = await PlayerService.createPlayer(nickname);
      setPlayer(newPlayer);
      console.log('Player created successfully:', newPlayer);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to create player';
      setError(errorMessage);
      console.error('Failed to create player:', errorMessage);
    } finally {
      setIsLoading(false);
    }
  }, []);

  /**
   * Updates the current player's score.
   * 
   * @param newScore - The new score value
   * @returns Promise that resolves when score is updated
   */
  const updateScore = useCallback(async (newScore: number): Promise<void> => {
    if (!player) {
      setError('No player selected');
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      const updatedPlayer = await PlayerService.updatePlayerScore(player.nickname, newScore);
      setPlayer(updatedPlayer);
      console.log('Player score updated:', updatedPlayer);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to update score';
      setError(errorMessage);
      console.error('Failed to update score:', errorMessage);
    } finally {
      setIsLoading(false);
    }
  }, [player]);

  /**
   * Loads an existing player by nickname.
   * 
   * @param nickname - Nickname of player to load
   * @returns Promise that resolves when player is loaded
   */
  const loadPlayer = useCallback(async (nickname: string): Promise<void> => {
    setIsLoading(true);
    setError(null);

    try {
      const existingPlayer = await PlayerService.getPlayerScore(nickname);
      setPlayer(existingPlayer);
      console.log('Player loaded successfully:', existingPlayer);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to load player';
      setError(errorMessage);
      console.error('Failed to load player:', errorMessage);
    } finally {
      setIsLoading(false);
    }
  }, []);

  /**
   * Checks if a player with given nickname exists.
   * 
   * @param nickname - Nickname to check
   * @returns Promise that resolves to boolean indicating existence
   */
  const checkPlayerExists = useCallback(async (nickname: string): Promise<boolean> => {
    try {
      return await PlayerService.playerExists(nickname);
    } catch (error) {
      console.error('Failed to check player existence:', error);
      return false;
    }
  }, []);

  /**
   * Clears the current player from state.
   * Useful for logging out or starting over.
   */
  const clearPlayer = useCallback(() => {
    setPlayer(null);
    setError(null);
  }, []);

  /**
   * Clears any error messages.
   */
  const clearError = useCallback(() => {
    setError(null);
  }, []);

  // Return hook interface
  return {
    // State
    player,
    isLoading,
    error,
    
    // Actions
    createPlayer,
    updateScore,
    loadPlayer,
    checkPlayerExists,
    clearPlayer,
    clearError,
    
    // Computed values
    hasPlayer: player !== null,
    playerName: player?.nickname || '',
    playerScore: player?.score || 0,
  };
};

export default usePlayer;