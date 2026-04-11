package cz.hackmeifyoucan.backend.dto.llm;

public record LlmGenerateSmsQuestionResponse(
        String sender,
        String phoneNumber,
        String content,
        String explanation,
        String category,
        String difficulty,
        boolean is_phishing
) implements LlmGenerateQuestionResponse {}


