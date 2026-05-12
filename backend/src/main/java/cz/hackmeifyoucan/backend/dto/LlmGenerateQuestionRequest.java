package cz.hackmeifyoucan.backend.dto;

import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.enums.PlatformType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Vstupní parametry pro generování otázky s Gemini LLM")
public record LlmGenerateQuestionRequest(
        @NotNull(message = "platform je povinné")
        @Parameter(description = "Cílová platforma")
        @Schema(example = "EMAIL")
        PlatformType platform,

        @NotBlank(message = "category je povinné")
        @Parameter(description = "Tag phishing kategorie. Povolené hodnoty odpovídají tagům v tabulce phishing_categories.")
        @Schema(allowableValues = {"LEGIT", "FAKE_URL", "URGENT", "FAKE_DOC", "CRED_THEFT", "SPEAR_PHISH", "LOTTERY"}, example = "URGENT")
        String category,

        @NotNull(message = "difficulty je povinné")
        @Parameter(description = "Obtížnost otázky")
        @Schema(allowableValues = {"EASY", "MEDIUM", "HARD"}, example = "HARD")
        Difficulty difficulty,

        @Parameter(description = "ISO 639-1 kód jazyka (default: cs).")
        @Schema(example = "cs", defaultValue = "cs")
        String language
) {
    public String getLanguageOrDefault() {
        return (language == null || language.isBlank()) ? "cs" : language;
    }
}

