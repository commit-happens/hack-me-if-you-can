package com.hackme.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * PlayerScoreRequest - Data Transfer Object for incoming API requests.
 * 
 * DTO CONCEPTS FOR BEGINNERS:
 * 
 * 1. WHAT IS A DTO?
 *    - Data Transfer Object: Simple object that carries data between layers
 *    - Used for API requests/responses (JSON ↔ Java object conversion)
 *    - Separates external API contract from internal entity structure
 *    - Provides validation and data transformation
 * 
 * 2. WHY SEPARATE DTOs FROM ENTITIES?
 *    - API Stability: Can change entity without breaking API
 *    - Security: Don't expose internal entity fields (like ID, timestamps)
 *    - Validation: Different rules for API input vs database storage
 *    - Flexibility: API might need different data structure than database
 * 
 * 3. VALIDATION ANNOTATIONS:
 *    - Automatically validate incoming JSON data
 *    - Spring Boot returns HTTP 400 if validation fails
 *    - Custom error messages for better user experience
 * 
 * 4. JSON MAPPING EXAMPLE:
 *    Request JSON: {"nickname": "player123", "score": 85}
 *    ↓ (Spring Boot converts automatically)
 *    PlayerScoreRequest object with nickname="player123", score=85
 * 
 * 5. USAGE FLOW:
 *    Frontend JSON → PlayerScoreRequest → Service Layer → Entity → Database
 * 
 * @author Hack Me If You Can Team
 * @version 1.0
 * @since Java 21
 */
public class PlayerScoreRequest {
    
    /**
     * Player's nickname for the game.
     * 
     * VALIDATION RULES:
     * - @NotBlank: Cannot be null, empty string, or only whitespace
     * - @Size: Must be between 3 and 50 characters
     * 
     * These validations ensure data quality before it reaches the business logic.
     * If validation fails, Spring Boot returns HTTP 400 Bad Request with error message.
     */
    @NotBlank(message = "Nickname cannot be blank")
    @Size(min = 3, max = 50, message = "Nickname must be between 3 and 50 characters")
    private String nickname;
    
    /**
     * Player's score for the phishing awareness game.
     * 
     * VALIDATION RULES:
     * - @NotNull: Score must be provided (can be 0, but not null)
     * 
     * Integer allows for large score values and null checking.
     * Business logic in service layer can add additional constraints (e.g., score range).
     */
    @NotNull(message = "Score cannot be null")
    private Integer score;
    
    public PlayerScoreRequest() {
    }
    
    public PlayerScoreRequest(String nickname, Integer score) {
        this.nickname = nickname;
        this.score = score;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
}