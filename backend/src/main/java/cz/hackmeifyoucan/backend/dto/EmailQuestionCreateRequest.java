package cz.hackmeifyoucan.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Request pro vytvoření emailové otázky")
public record EmailQuestionCreateRequest(
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
        @Size(max = 3000, message = "content nesmí překročit 3000 znaků")
        String content,

        @Schema(description = "Vysvětlení proč jde/nejde o phishing", example = "Nátlak na okamžitou akci")
        @NotBlank(message = "explanation je povinné")
        @Size(max = 3000, message = "explanation nesmí překročit 3000 znaků")
        String explanation,

        @Schema(description = "Legacy ID kategorie. Volitelné, preferovaný je category_tag.", example = "3")
        @Positive(message = "category_id musí být kladné číslo")
        @JsonProperty("category_id")
        Long categoryId,

        @Schema(description = "Tag kategorie (case-insensitive), mapuje se na category_id.", example = "URGENT")
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



