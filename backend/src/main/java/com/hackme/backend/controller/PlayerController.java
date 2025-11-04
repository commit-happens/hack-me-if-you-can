package com.hackme.backend.controller;

import com.hackme.backend.dto.PlayerScoreRequest;
import com.hackme.backend.dto.PlayerScoreResponse;
import com.hackme.backend.service.PlayerNotFoundException;
import com.hackme.backend.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * PlayerController - REST API Controller for player management.
 * 
 * CONTROLLER CONCEPTS FOR BEGINNERS:
 * 
 * 1. WHAT IS A CONTROLLER?
 *    - A controller handles HTTP requests from the frontend/clients
 *    - It's the "front desk" of your application
 *    - Maps URLs to specific methods
 *    - Processes input, calls business logic, returns responses
 * 
 * 2. REST API PRINCIPLES:
 *    - REST = Representational State Transfer
 *    - Uses HTTP methods: GET (read), POST (create), PUT (update), DELETE (remove)
 *    - URLs represent resources: /players represents player collection
 *    - Responses are typically JSON format
 * 
 * 3. KEY ANNOTATIONS EXPLAINED:
 *    - @RestController: Combines @Controller + @ResponseBody (returns JSON)
 *    - @RequestMapping: Base URL path for all methods in this controller
 *    - @CrossOrigin: Allows requests from different domains (CORS policy)
 *    - @GetMapping, @PostMapping, @DeleteMapping: Specific HTTP method mappings
 * 
 * 4. HTTP REQUEST/RESPONSE FLOW:
 *    Frontend Request → Controller Method → Service Layer → Repository → Database
 *    Database Response ← Repository ← Service Layer ← Controller Method ← Frontend
 * 
 * 5. EXAMPLE API CALLS:
 *    GET /players/john123/score      → Get John's score
 *    POST /players/score            → Create/update a player score
 *    DELETE /players/john123/score  → Remove John from game
 *    GET /players/john123/exists    → Check if John exists
 * 
 * @author Hack Me If You Can Team
 * @version 1.0
 * @since Java 21
 */
@RestController // Tells Spring this class handles REST API requests
@RequestMapping("/players") // Base URL path: all methods will start with /players
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}) // Allow frontend requests
public class PlayerController {
    
    /**
     * PlayerService dependency - contains business logic.
     * 
     * The 'final' keyword means this reference cannot be changed after construction.
     * This is a best practice for dependency injection.
     */
    private final PlayerService playerService;
    
    /**
     * Constructor with dependency injection.
     * 
     * @Autowired tells Spring to automatically inject a PlayerService instance.
     * This is called "Constructor Injection" and is the recommended approach.
     * 
     * WHY DEPENDENCY INJECTION?
     * - Loose coupling: Controller doesn't create its dependencies
     * - Testability: Easy to inject mock services for testing
     * - Flexibility: Spring manages object lifecycle and configuration
     * 
     * @param playerService The service layer for player operations
     */
    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    /**
     * GET /players/{nickname}/score - Retrieves a player's score by nickname.
     * 
     * ENDPOINT DETAILS:
     * - URL: GET /players/john123/score
     * - Purpose: Get the current score for player "john123"
     * - Response: JSON with player data or 404 if not found
     * 
     * ANNOTATIONS EXPLAINED:
     * - @GetMapping: Maps HTTP GET requests to this method
     * - @PathVariable: Extracts {nickname} from URL into method parameter
     * 
     * RESPONSE EXAMPLES:
     * Success (200): {"nickname": "john123", "score": 85}
     * Not Found (404): (empty response body)
     * 
     * @param nickname The player's unique nickname from the URL path
     * @return ResponseEntity containing PlayerScoreResponse or error status
     */
    @GetMapping("/{nickname}/score")
    public ResponseEntity<PlayerScoreResponse> getPlayerScore(@PathVariable String nickname) {
        try {
            // Call business logic layer to get player data
            PlayerScoreResponse response = playerService.getPlayerScore(nickname);
            // Return success response with data (HTTP 200)
            return ResponseEntity.ok(response);
        } catch (PlayerNotFoundException e) {
            // Player doesn't exist - return HTTP 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * POST /players/score - Creates a new player or updates existing player's score.
     * 
     * ENDPOINT DETAILS:
     * - URL: POST /players/score
     * - Purpose: Create new player or update existing one
     * - Request Body: JSON with nickname and score
     * - Response: JSON with updated player data
     * 
     * ANNOTATIONS EXPLAINED:
     * - @PostMapping: Maps HTTP POST requests to this method
     * - @Valid: Validates the request body using validation annotations
     * - @RequestBody: Converts JSON request body to Java object
     * 
     * REQUEST EXAMPLE:
     * POST /players/score
     * Content-Type: application/json
     * {"nickname": "newplayer", "score": 75}
     * 
     * RESPONSE EXAMPLE:
     * Success (200): {"nickname": "newplayer", "score": 75}
     * 
     * @param request The player data from request body (validated)
     * @return ResponseEntity containing PlayerScoreResponse or error status
     */
    @PostMapping("/score")
    public ResponseEntity<PlayerScoreResponse> createOrUpdatePlayerScore(
            @Valid @RequestBody PlayerScoreRequest request) {
        try {
            // Call business logic to create or update player
            PlayerScoreResponse response = playerService.createOrUpdatePlayerScore(request);
            // Return success response with updated data (HTTP 200)
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Something went wrong - return HTTP 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * DELETE /players/{nickname}/score - Removes a player from the system.
     * 
     * ENDPOINT DETAILS:
     * - URL: DELETE /players/john123/score
     * - Purpose: Remove player "john123" completely
     * - Response: JSON with success message or 404 if not found
     * 
     * ANNOTATIONS EXPLAINED:
     * - @DeleteMapping: Maps HTTP DELETE requests to this method
     * - @PathVariable: Extracts {nickname} from URL
     * 
     * RESPONSE EXAMPLES:
     * Success (200): {"message": "Player john123 deleted successfully"}
     * Not Found (404): (empty response body)
     * 
     * @param nickname The player's unique nickname to delete
     * @return ResponseEntity with success message or error status
     */
    @DeleteMapping("/{nickname}/score")
    public ResponseEntity<Map<String, String>> deletePlayerScore(@PathVariable String nickname) {
        try {
            // Call business logic to delete player
            String message = playerService.deletePlayer(nickname);
            // Create response map with success message
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            // Return success response (HTTP 200)
            return ResponseEntity.ok(response);
        } catch (PlayerNotFoundException e) {
            // Player doesn't exist - create error response
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            // Return HTTP 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /players/{nickname}/exists - Checks if a player exists in the system.
     * 
     * ENDPOINT DETAILS:
     * - URL: GET /players/john123/exists
     * - Purpose: Check if player "john123" exists (without getting full data)
     * - Response: JSON with boolean result
     * 
     * RESPONSE EXAMPLES:
     * Player exists: {"exists": true}
     * Player doesn't exist: {"exists": false}
     * 
     * This is useful for:
     * - Form validation on frontend
     * - Checking before creating new players
     * - Quick existence checks without full data transfer
     * 
     * @param nickname The player's unique nickname to check
     * @return ResponseEntity with boolean result indicating if player exists
     */
    @GetMapping("/{nickname}/exists")
    public ResponseEntity<Map<String, Boolean>> playerExists(@PathVariable String nickname) {
        // Call business logic to check existence
        boolean exists = playerService.playerExists(nickname);
        // Create response map with boolean result
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        // Always return success (HTTP 200) - existence check never fails
        return ResponseEntity.ok(response);
    }
}