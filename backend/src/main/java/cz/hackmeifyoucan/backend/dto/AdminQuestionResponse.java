package cz.hackmeifyoucan.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Response s uloženou otázkou v admin API")
public record AdminQuestionResponse(

        @Schema(description = "ID otázky", example = "101")
        Long id,

        @Schema(description = "Platforma otázky", example = "email")
        String platform,

        @Schema(description = "Obtížnost", example = "HARD")
        String difficulty,

        @Schema(description = "ID phishing kategorie", example = "3")
        @JsonProperty("category_id")
        Long categoryId,

        @Schema(description = "Indikace phishingu", example = "true")
        @JsonProperty("is_phishing")
        boolean phishing,

        @Schema(description = "Doplňková metadata podle platformy (např. sender/subject nebo sender/phoneNumber)")
        Map<String, String> metadata,

        @Schema(description = "Obsah otázky", example = "Please verify your account immediately")
        String content,

        @Schema(description = "Vysvětlení otázky", example = "Nátlak na okamžitou akci")
        String explanation,

        @Schema(description = "Penalty body", example = "0")
        int penalty,

        @Schema(description = "Čas vytvoření", example = "2026-04-04T12:00:00")
        LocalDateTime createdAt
) {
}



