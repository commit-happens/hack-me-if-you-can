package cz.hackmeifyoucan.backend.llm.prompt;

import cz.hackmeifyoucan.backend.enums.PlatformType;

public interface PromptTemplate {
    PlatformType platform();

    String render(String category, String difficulty, String language);
}

