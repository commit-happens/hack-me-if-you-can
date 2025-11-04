package com.hackme.backend.repository;

import com.hackme.backend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * PlayerRepository - Data Access Layer for Player entity.
 * 
 * REPOSITORY CONCEPTS FOR BEGINNERS:
 * 
 * 1. WHAT IS A REPOSITORY?
 *    - Interface that provides database operations (CRUD)
 *    - Abstracts database complexity from business logic
 *    - Spring Data JPA creates implementation automatically
 *    - No need to write SQL queries for basic operations!
 * 
 * 2. JpaRepository INHERITANCE:
 *    - Extends JpaRepository<Entity, IDType>
 *    - Provides built-in methods: save(), findById(), findAll(), delete(), etc.
 *    - Generic type parameters: Player (entity), Long (ID type)
 * 
 * 3. CUSTOM QUERY METHODS:
 *    - Spring Data JPA generates SQL from method names
 *    - Method name conventions: findBy, existsBy, deleteBy, countBy
 *    - Field name matching: findByNickname → WHERE nickname = ?
 *    - Modifiers: IgnoreCase → case-insensitive comparison
 * 
 * 4. METHOD NAME PATTERNS:
 *    - findByNicknameIgnoreCase → SELECT * FROM players WHERE LOWER(nickname) = LOWER(?)
 *    - existsByNicknameIgnoreCase → SELECT COUNT(*) > 0 FROM players WHERE LOWER(nickname) = LOWER(?)
 *    - deleteByNicknameIgnoreCase → DELETE FROM players WHERE LOWER(nickname) = LOWER(?)
 * 
 * 5. RETURN TYPES:
 *    - Optional<Entity>: Might return null, safely handled
 *    - boolean: Simple true/false result
 *    - void: No return value (for delete operations)
 *    - List<Entity>: Multiple results
 * 
 * SPRING DATA JPA MAGIC:
 * You write interfaces, Spring creates implementations with proper SQL queries!
 * 
 * @author Hack Me If You Can Team
 * @version 1.0
 * @since Java 21
 */
@Repository // Tells Spring this is a repository component (data access layer)
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    /**
     * Finds a player by their nickname (case-insensitive search).
     * 
     * SPRING DATA JPA MAGIC:
     * Method name "findByNicknameIgnoreCase" automatically generates SQL:
     * SELECT * FROM players WHERE LOWER(nickname) = LOWER(?1)
     * 
     * WHY Optional<Player>?
     * - Player might not exist (avoid NullPointerException)
     * - Forces caller to handle "not found" scenario
     * - Modern Java best practice for "might be null" situations
     * 
     * CASE-INSENSITIVE:
     * "IgnoreCase" means "John", "john", "JOHN" all match
     * 
     * @param nickname The player's nickname to search for
     * @return Optional containing Player if found, empty Optional if not found
     */
    Optional<Player> findByNicknameIgnoreCase(String nickname);
    
    /**
     * Checks if a player exists with the given nickname (case-insensitive).
     * 
     * SPRING DATA JPA MAGIC:
     * Method name "existsByNicknameIgnoreCase" generates SQL:
     * SELECT COUNT(*) > 0 FROM players WHERE LOWER(nickname) = LOWER(?1)
     * 
     * PERFORMANCE BENEFIT:
     * More efficient than findBy when you only need to check existence.
     * Database returns boolean instead of full entity data.
     * 
     * USE CASES:
     * - Form validation ("Username already taken")
     * - Pre-checks before creating players
     * - Quick existence verification
     * 
     * @param nickname The nickname to check for existence
     * @return true if a player with this nickname exists, false otherwise
     */
    boolean existsByNicknameIgnoreCase(String nickname);
    
    /**
     * Deletes a player by their nickname (case-insensitive).
     * 
     * SPRING DATA JPA MAGIC:
     * Method name "deleteByNicknameIgnoreCase" generates SQL:
     * DELETE FROM players WHERE LOWER(nickname) = LOWER(?1)
     * 
     * TRANSACTION SAFETY:
     * This method must be called within a @Transactional method
     * (our service methods are already @Transactional)
     * 
     * IMPORTANT NOTES:
     * - This is a destructive operation (data permanently lost)
     * - Returns void (no return value)
     * - If nickname doesn't exist, operation silently succeeds
     * - Use service layer to check existence before deletion
     * 
     * @param nickname The nickname of the player to delete
     */
    void deleteByNicknameIgnoreCase(String nickname);
}