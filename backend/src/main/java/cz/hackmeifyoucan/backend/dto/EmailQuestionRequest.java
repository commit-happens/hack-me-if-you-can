package cz.hackmeifyoucan.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import cz.hackmeifyoucan.backend.enums.Difficulty;

@Schema(description = "Request pro vytvoření emailové otázky")
public record EmailQuestionRequest(
        @Schema(description = "Předmět emailu", example = "Urgent account verification")
        @NotBlank(message = "subject je povinné")
        @Size(max = 255, message = "subject nesmí překročit 255 znaků")
        String subject,

        @Schema(description = "Odesílatel emailu", example = "security@acme.com")
        @NotBlank(message = "sender je povinné")
        @Size(max = 255, message = "sender nesmí překročit 255 znaků")
        String sender,

        @Schema(description = "Text emailu", example = "Please verify your account immediately")
        @NotBlank(message = "content je povinné")
        @Size(max = 1000, message = "content nesmí překročit 1000 znaků")
        String content,

        @Schema(description = "Vysvětlení proč jde/nejde o phishing", example = "Nátlak na okamžitou akci")
        @NotBlank(message = "explanation je povinné")
        @Size(max = 2000, message = "explanation nesmí překročit 2000 znaků")
        String explanation,

        @Schema(description = "ID kategorie (volitelné, pokud není poslán tag).", example = "1")
        @Positive(message = "category_id musí být kladné číslo")
        @JsonProperty("category_id")
        Long categoryId,

        @Schema(description = "Tag kategorie (case-insensitive). Pokud není poslán id, použije se tag.", example = "URGENT")
        @Size(max = 30, message = "category_tag nesmí překročit 30 znaků")
        @JsonProperty("category_tag")
        String categoryTag,

        @Schema(description = "Obtížnost", example = "HARD")
        @NotNull(message = "difficulty je povinné")
        Difficulty difficulty,

        @Schema(description = "Indikace phishingu", example = "true")
        @NotNull(message = "is_phishing je povinné")
        @JsonProperty("is_phishing")
        Boolean phishing
) {
}
