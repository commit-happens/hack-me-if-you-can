package cz.hackmeifyoucan.backend.exception;

public class InvalidQuestionParameterException extends RuntimeException {
    public InvalidQuestionParameterException(String message) {
        super(message);
    }
}
