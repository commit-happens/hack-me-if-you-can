package cz.hackmeifyoucan.backend.exception;

public class DuplicateAnswerException extends RuntimeException {

    public DuplicateAnswerException(Long playerId, Long questionId, String sessionId) {
        super("Odpověď již existuje pro player_id=" + playerId + ", question_id=" + questionId + " a session_id=" + sessionId);
    }
}


