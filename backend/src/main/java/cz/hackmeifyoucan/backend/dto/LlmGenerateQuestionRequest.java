package cz.hackmeifyoucan.backend.dto;

import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.enums.PlatformType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

public record LlmGenerateQuestionRequest(
        @Parameter(description = "Target platform")
        PlatformType platform,
        @Parameter(
                description = "Tag phishing kategorie. Povolené hodnoty odpovídají tagům v tabulce phishing_categories.",
                schema = @Schema(allowableValues = {
                        "LEGIT", "FAKE_URL", "URGENT", "FAKE_DOC", "CRED_THEFT", "SPEAR_PHISH", "LOTTERY"
                })
        )
        String category,
        @Parameter(
                description = "Question difficulty",
                schema = @Schema(allowableValues = {"EASY", "MEDIUM", "HARD"})
        )
        Difficulty difficulty,
        @Parameter(description = "ISO 639-1 language code")
        String language
) {
}


