package cz.hackmeifyoucan.backend.dto;

public record LlmGenerateEmailQuestionResponse(
        String subject,
        String sender,
        String content,
        String explanation,
        String category,
        String difficulty,
        boolean is_phishing
) {}

