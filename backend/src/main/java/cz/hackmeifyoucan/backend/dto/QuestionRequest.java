package cz.hackmeifyoucan.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request pro vytvoření otázky")
public record QuestionRequest(
        @Schema(description = "Předmět nebo název", example = "Urgent account verification")
        @NotBlank(message = "subject je povinné")
        @Size(max = 255, message = "subject nesmí překročit 255 znaků")
        String subject,

        @Schema(description = "Odesílatel emailu nebo Telefonní číslo", example = "security@acme.com")
        @NotBlank(message = "sender je povinné")
        @Size(max = 255, message = "sender nesmí překročit 255 znaků")
        String sender,

        @Schema(description = "Text otázky", example = "Please verify your account immediately")
        @NotBlank(message = "content je povinné")
        @Size(max = 1000, message = "content nesmí překročit 1000 znaků")
        String content,

        @Schema(description = "Vysvětlení proč jde/nejde o phishing", example = "Nátlak na okamžitou akci")
        @NotBlank(message = "explanation je povinné")
        @Size(max = 2000, message = "explanation nesmí překročit 2000 znaků")
        String explanation,

        @Schema(description = "Tag kategorie (case-insensitive).", example = "URGENT")
        @NotBlank(message = "category_tag je povinné")
        @Size(max = 30, message = "category_tag nesmí překročit 30 znaků")
        @JsonProperty("category_tag")
        String categoryTag,

        @Schema(description = "Obtížnost", example = "HARD", allowableValues = {"EASY", "MEDIUM", "HARD"})
        @NotBlank(message = "difficulty je povinné")
        @Size(max = 20, message = "difficulty nesmí překročit 20 znaků")
        String difficulty,

        @Schema(description = "Indikace phishingu", example = "true")
        @NotNull(message = "is_phishing je povinné")
        @JsonProperty("is_phishing")
        Boolean phishing
) {
}