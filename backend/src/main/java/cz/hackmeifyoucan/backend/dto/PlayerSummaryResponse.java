package cz.hackmeifyoucan.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record PlayerSummaryResponse(
        @Schema(description = "ID hráče", example = "1")
        @JsonProperty("player_id")
        Long playerId,

        @Schema(description = "Identifikátor session, pro kterou je summary počítáno", example = "d14f3b7f4d6f4f16a3d0c8ef9c2b6e1a")
        @JsonProperty("session_id")
        String sessionId,

        @Schema(description = "Aktuální skóre hráče", example = "2560")
        int score,

        @Schema(description = "Potenciální skóre (aktuální score + body z chybně zodpovězených otázek bez speed bonusu)", example = "3200")
        @JsonProperty("potential_score")
        int potentialScore
) {
}


