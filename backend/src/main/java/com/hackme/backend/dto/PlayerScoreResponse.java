package com.hackme.backend.dto;

/**
 * PlayerScoreResponse - Data Transfer Object for outgoing API responses.
 * 
 * RESPONSE DTO CONCEPTS:
 * 
 * 1. PURPOSE:
 *    - Standardizes API response format
 *    - Controls what data is exposed to frontend
 *    - Provides consistent JSON structure
 *    - Can include additional metadata (messages, timestamps)
 * 
 * 2. SECURITY BENEFITS:
 *    - Doesn't expose internal entity fields (like database ID)
 *    - No sensitive information accidentally leaked
 *    - Clean, minimal API contract
 * 
 * 3. JSON OUTPUT EXAMPLE:
 *    PlayerScoreResponse → JSON:
 *    {
 *      "nickname": "player123",
 *      "score": 85,
 *      "message": "Score updated successfully"
 *    }
 * 
 * 4. FLEXIBILITY:
 *    - Multiple constructors for different use cases
 *    - Optional message field for operation feedback
 *    - Easy to extend with additional fields
 * 
 * 5. USAGE SCENARIOS:
 *    - GET /players/{nickname}/score → Returns player data
 *    - POST /players/score → Returns updated player data + success message
 *    - Consistent format across all player-related endpoints
 * 
 * @author Hack Me If You Can Team
 * @version 1.0
 * @since Java 21
 */
public class PlayerScoreResponse {
    
    /**
     * Player's nickname - matches the request/entity nickname.
     * This is the public identifier for the player.
     */
    private String nickname;
    
    /**
     * Player's current score in the phishing awareness game.
     * Represents their performance/progress.
     */
    private Integer score;
    
    /**
     * Optional message for operation feedback.
     * Used to inform frontend about operation results:
     * - "Player created successfully"
     * - "Score updated successfully"
     * - Custom success/info messages
     */
    private String message;
    
    /**
     * CONSTRUCTORS - Different ways to create response objects
     */
    
    /**
     * Default constructor for JSON deserialization.
     * Spring Boot needs this to convert JSON to Java objects.
     */
    public PlayerScoreResponse() {
        // Empty constructor for frameworks
    }
    
    /**
     * Constructor for basic player data without message.
     * 
     * Used for simple queries like GET /players/{nickname}/score
     * where we only need to return player data.
     * 
     * @param nickname Player's nickname
     * @param score    Player's current score
     */
    public PlayerScoreResponse(String nickname, Integer score) {
        this.nickname = nickname;
        this.score = score;
        // message remains null
    }
    
    /**
     * Constructor for operations that need feedback message.
     * 
     * Used for create/update operations like POST /players/score
     * where we want to inform the user about the operation result.
     * 
     * @param nickname Player's nickname
     * @param score    Player's current score
     * @param message  Success/info message about the operation
     */
    public PlayerScoreResponse(String nickname, Integer score, String message) {
        this.nickname = nickname;
        this.score = score;
        this.message = message;
    }
    
    /**
     * GETTER AND SETTER METHODS
     * 
     * Required for JSON serialization/deserialization.
     * Spring Boot uses these methods to convert between JSON and Java objects.
     */
    
    /**
     * Gets the player's nickname.
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }
    
    /**
     * Sets the player's nickname.
     * @param nickname the player's nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    /**
     * Gets the player's score.
     * @return the player's current score
     */
    public Integer getScore() {
        return score;
    }
    
    /**
     * Sets the player's score.
     * @param score the player's score
     */
    public void setScore(Integer score) {
        this.score = score;
    }
    
    /**
     * Gets the operation message.
     * @return operation feedback message (may be null)
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the operation message.
     * @param message feedback message about the operation
     */
    public void setMessage(String message) {
        this.message = message;
    }
}