package cz.hackmeifyoucan.backend.dto.llm;

public record LlmGenerateEmailQuestionResponse(
        String subject,
        String sender,
        String content,
        String explanation,
        String category,
        String difficulty,
        boolean is_phishing
) implements LlmGenerateQuestionResponse {}


