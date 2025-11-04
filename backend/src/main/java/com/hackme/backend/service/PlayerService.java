package com.hackme.backend.service;

import com.hackme.backend.dto.PlayerScoreRequest;
import com.hackme.backend.dto.PlayerScoreResponse;
import com.hackme.backend.entity.Player;
import com.hackme.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * PlayerService - Business Logic Layer for player operations.
 * 
 * SERVICE LAYER CONCEPTS FOR BEGINNERS:
 * 
 * 1. WHAT IS A SERVICE LAYER?
 *    - Contains business logic and rules
 *    - Acts as a bridge between Controllers and Repositories
 *    - Handles complex operations that involve multiple steps
 *    - Manages transactions (database operations that must succeed/fail together)
 * 
 * 2. WHY SEPARATE SERVICE LAYER?
 *    - Controllers should be thin (just handle HTTP requests/responses)
 *    - Business logic should be reusable (not tied to web layer)
 *    - Easier to test business logic separately
 *    - Better organization and maintainability
 * 
 * 3. KEY ANNOTATIONS EXPLAINED:
 *    - @Service: Marks this as a service component for Spring
 *    - @Transactional: Ensures database operations are atomic (all succeed or all fail)
 * 
 * 4. BUSINESS LOGIC EXAMPLES:
 *    - Validate player data before saving
 *    - Handle player not found scenarios
 *    - Convert between entities and DTOs
 *    - Apply business rules (e.g., score limits, nickname validation)
 * 
 * 5. TRANSACTION MANAGEMENT:
 *    - @Transactional ensures data consistency
 *    - If any operation fails, all changes are rolled back
 *    - Example: If updating player fails, don't create audit log
 * 
 * LAYER INTERACTION:
 * Controller → Service (business logic) → Repository (data access) → Database
 * 
 * @author Hack Me If You Can Team
 * @version 1.0
 * @since Java 21
 */
@Service // Tells Spring this is a service component (business logic layer)
@Transactional // Makes all methods transactional by default (database operations are atomic)
public class PlayerService {
    
    /**
     * PlayerRepository dependency for database operations.
     * 
     * The service uses repository to access data but doesn't know
     * about database details (SQL, connections, etc.)
     */
    private final PlayerRepository playerRepository;
    
    /**
     * Constructor with dependency injection.
     * 
     * Spring automatically injects the PlayerRepository implementation.
     * The repository will be a proxy created by Spring Data JPA.
     * 
     * @param playerRepository Repository for player data access
     */
    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
    
    /**
     * Retrieves a player's score by their nickname.
     * 
     * BUSINESS LOGIC:
     * 1. Search for player by nickname (case-insensitive)
     * 2. If found, convert Entity to DTO and return
     * 3. If not found, throw custom exception
     * 
     * CONCEPTS EXPLAINED:
     * - Optional<Player>: Java 8+ way to handle "might not exist" scenarios
     * - DTO Conversion: Convert internal Entity to external response format
     * - Exception Handling: Use custom exceptions for business rule violations
     * 
     * @param nickname The player's unique nickname (case-insensitive)
     * @return PlayerScoreResponse DTO containing nickname and score
     * @throws PlayerNotFoundException if no player exists with that nickname
     */
    public PlayerScoreResponse getPlayerScore(String nickname) {
        // Call repository to find player (returns Optional to handle not found)
        Optional<Player> playerOpt = playerRepository.findByNicknameIgnoreCase(nickname);
        
        // Check if player was found using Optional.isPresent()
        if (playerOpt.isPresent()) {
            // Extract the Player object from Optional
            Player player = playerOpt.get();
            // Convert Entity to DTO (Data Transfer Object for API response)
            return new PlayerScoreResponse(player.getNickname(), player.getScore());
        } else {
            // Player not found - throw custom business exception
            // This will be caught by the controller and converted to HTTP 404
            throw new PlayerNotFoundException("Player with nickname '" + nickname + "' not found");
        }
    }
    
    /**
     * Creates a new player or updates an existing player's score.
     * 
     * BUSINESS LOGIC:
     * 1. Check if player already exists (by nickname)
     * 2. If exists: Update their score
     * 3. If new: Create new player with given score
     * 4. Save to database and return response
     * 
     * This is an "UPSERT" operation (UPDATE or INSERT).
     * 
     * TRANSACTION SAFETY:
     * The @Transactional annotation ensures this entire method
     * runs in a single database transaction. If any step fails,
     * all changes are rolled back.
     * 
     * @param request DTO containing nickname and score from API request
     * @return PlayerScoreResponse DTO with updated player data and success message
     */
    public PlayerScoreResponse createOrUpdatePlayerScore(PlayerScoreRequest request) {
        // Try to find existing player (case-insensitive search)
        Optional<Player> existingPlayerOpt = playerRepository.findByNicknameIgnoreCase(request.getNickname());
        
        Player player;
        String message;
        
        // Determine if this is an update or create operation
        if (existingPlayerOpt.isPresent()) {
            // UPDATE: Player exists, modify their score
            player = existingPlayerOpt.get();
            player.setScore(request.getScore()); // Update score field
            message = "Score updated successfully";
        } else {
            // CREATE: New player, create new entity
            player = new Player(request.getNickname(), request.getScore());
            message = "Player created successfully";
        }
        
        // Save to database (works for both create and update)
        // JPA will INSERT if entity is new, UPDATE if entity exists
        player = playerRepository.save(player);
        
        // Convert Entity back to DTO for response
        return new PlayerScoreResponse(player.getNickname(), player.getScore(), message);
    }
    
    /**
     * Deletes a player from the system by nickname.
     * 
     * BUSINESS LOGIC:
     * 1. Check if player exists
     * 2. If exists: Delete from database
     * 3. If not exists: Throw exception
     * 
     * IMPORTANT: This is a destructive operation!
     * Once deleted, all player data is permanently lost.
     * 
     * @param nickname The nickname of the player to delete
     * @return Success message confirming deletion
     * @throws PlayerNotFoundException if no player exists with that nickname
     */
    public String deletePlayer(String nickname) {
        // First check if player exists
        Optional<Player> playerOpt = playerRepository.findByNicknameIgnoreCase(nickname);
        
        if (playerOpt.isPresent()) {
            // Player exists - proceed with deletion
            playerRepository.deleteByNicknameIgnoreCase(nickname);
            return "Player '" + nickname + "' deleted successfully";
        } else {
            // Player doesn't exist - throw business exception
            throw new PlayerNotFoundException("Player with nickname '" + nickname + "' not found");
        }
    }
    
    /**
     * Checks if a player exists in the system by nickname.
     * 
     * This is a lightweight operation that only checks existence
     * without loading the full player data. Useful for:
     * - Form validation on frontend
     * - Pre-checks before operations
     * - Quick existence verification
     * 
     * @param nickname The nickname to check (case-insensitive)
     * @return true if player exists, false otherwise
     */
    public boolean playerExists(String nickname) {
        // Use repository's exists method - more efficient than findBy
        return playerRepository.existsByNicknameIgnoreCase(nickname);
    }
}