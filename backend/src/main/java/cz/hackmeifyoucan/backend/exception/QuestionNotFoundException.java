package cz.hackmeifyoucan.backend.exception;

public class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException(Long questionId) {
        super("Otázka nenalezena pro ID: " + questionId);
    }
}

