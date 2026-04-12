package cz.hackmeifyoucan.backend.exception;

public class PhishingCategoryNotFoundException extends RuntimeException {

    public PhishingCategoryNotFoundException(String tag) {
        super("Phishing category not found for tag: " + tag);
    }
}

