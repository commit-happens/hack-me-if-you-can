package cz.hackmeifyoucan.backend.dto.llm;

import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.enums.PlatformType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Query parametry pro LLM generování otázky")
public record LlmGenerateQuestionRequest(
        @Parameter(description = "Cílová platforma")
        @Schema(example = "EMAIL", allowableValues = {"EMAIL", "SMS"})
        PlatformType platform,
        @Parameter(
                description = "Tag phishing kategorie. Povolené hodnoty odpovídají tagům v tabulce phishing_categories.",
                schema = @Schema(allowableValues = {
                        "LEGIT", "FAKE_URL", "URGENT", "FAKE_DOC", "CRED_THEFT", "SPEAR_PHISH", "LOTTERY"
                }, example = "URGENT")
        )
        String category,
        @Parameter(
                description = "Obtížnost otázky",
                schema = @Schema(allowableValues = {"EASY", "MEDIUM", "HARD"}, example = "HARD")
        )
        Difficulty difficulty,
        @Parameter(description = "ISO 639-1 language code. Pokud není zadáno, použije se cs.")
        @Schema(example = "cs", defaultValue = "cs")
        String language
) {
    // Helper to keep logic out of the controller
    public String getLanguageOrDefault() {
        return (language == null || language.isBlank()) ? "cs" : language;
    }
}


