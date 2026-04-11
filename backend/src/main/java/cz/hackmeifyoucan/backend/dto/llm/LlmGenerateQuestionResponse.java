package cz.hackmeifyoucan.backend.dto.llm;

public interface LlmGenerateQuestionResponse {
    String content();
    String explanation();
    String category();
    String difficulty();
    boolean is_phishing();
}


