/**
 * PlayerNotFoundException - výjimka pro situaci, kdy hráč není nalezen.
 */

package cz.hackmeifyoucan.backend.exception;

public class PlayerNotFoundException extends RuntimeException {
    
    public PlayerNotFoundException(String message) {
        super(message);
    }
    
    public PlayerNotFoundException(Long playerId) {
        super("Hráč nenalezen pro ID: " + playerId);
    }
}
