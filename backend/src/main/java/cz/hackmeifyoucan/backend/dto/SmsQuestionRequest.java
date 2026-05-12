package cz.hackmeifyoucan.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import cz.hackmeifyoucan.backend.enums.Difficulty;

@Schema(description = "Request pro vytvoření SMS otázky")
public record SmsQuestionRequest(
        @Schema(description = "Jméno odesílatele SMS", example = "InfoSMS")
        @NotBlank(message = "sender je povinné")
        @Size(max = 255, message = "sender nesmí překročit 255 znaků")
        String sender,

        @Schema(description = "Telefonní číslo odesílatele", example = "+420123456789")
        @NotBlank(message = "phoneNumber je povinné")
        @Size(max = 64, message = "phoneNumber nesmí překročit 64 znaků")
        String phoneNumber,

        @Schema(description = "Text SMS", example = "Klikněte na odkaz")
        @NotBlank(message = "content je povinné")
        @Size(max = 1000, message = "content nesmí překročit 1000 znaků")
        String content,

        @Schema(description = "Vysvětlení proč jde/nejde o phishing", example = "Podezřelý odkaz")
        @NotBlank(message = "explanation je povinné")
        @Size(max = 2000, message = "explanation nesmí překročit 2000 znaků")
        String explanation,

        @Schema(description = "ID kategorie (volitelné, pokud není poslán tag).", example = "2")
        @Positive(message = "category_id musí být kladné číslo")
        @JsonProperty("category_id")
        Long categoryId,

        @Schema(description = "Tag kategorie (case-insensitive). Pokud není poslán id, použije se tag.", example = "FAKE_URL")
        @Size(max = 30, message = "category_tag nesmí překročit 30 znaků")
        @JsonProperty("category_tag")
        String categoryTag,

        @Schema(description = "Obtížnost", example = "EASY")
        @NotNull(message = "difficulty je povinné")
        Difficulty difficulty,

        @Schema(description = "Indikace phishingu", example = "true")
        @NotNull(message = "is_phishing je povinné")
        @JsonProperty("is_phishing")
        Boolean phishing
) {
}
