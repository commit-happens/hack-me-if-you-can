package cz.hackmeifyoucan.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.hackmeifyoucan.backend.enums.PlatformType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request pro vytvoření otázky generované LLM")
public record QuestionRequest(
        @Schema(description = "Platforma (email nebo sms)", example = "email", allowableValues = {"email", "sms"})
        @NotBlank(message = "platform je povinné")
        @JsonProperty("platform")
        String platform,

        @Schema(description = "Metadata (sender + subject)", implementation = QuestionMetadataResponse.class)
        @NotNull(message = "metadata je povinné")
        QuestionMetadataResponse metadata,

        @Schema(description = "Text otázky", example = "Please verify your account immediately")
        @NotBlank(message = "content je povinné")
        @Size(max = 1000, message = "content nesmí překročit 1000 znaků")
        String content,

        @Schema(description = "Vysvětlení proč jde/nejde o phishing", example = "Nátlak na okamžitou akci")
        @NotBlank(message = "explanation je povinné")
        @Size(max = 2000, message = "explanation nesmí překročit 2000 znaků")
        String explanation,

        @Schema(description = "Tag kategorie (case-insensitive).", example = "URGENT")
        @NotBlank(message = "category je povinné")
        @Size(max = 30, message = "category nesmí překročit 30 znaků")
        String category,

        @Schema(description = "Obtížnost", example = "MEDIUM", allowableValues = {"EASY", "MEDIUM", "HARD"})
        @NotBlank(message = "difficulty je povinné")
        @Size(max = 20, message = "difficulty nesmí překročit 20 znaků")
        String difficulty,

        @Schema(description = "Indikace phishingu", example = "true")
        @NotNull(message = "is_phishing je povinné")
        @JsonProperty("is_phishing")
        Boolean is_phishing
) {
    public PlatformType getPlatformType() {
        return PlatformType.fromName(this.platform);
    }
}

