package cz.hackmeifyoucan.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnswerRequest(
        @NotNull(message = "player_id je povinné")
        @Schema(description = "ID hráče", example = "1")
        @JsonProperty("player_id")
        Long playerId,

        @NotNull(message = "question_id je povinné")
        @Schema(description = "ID otázky", example = "3")
        @JsonProperty("question_id")
        Long questionId,

        @NotBlank(message = "session_id je povinné")
        @Size(max = 64, message = "session_id nesmí překročit 64 znaků")
        @Schema(description = "Identifikátor herní session generovaný frontendem", example = "d14f3b7f4d6f4f16a3d0c8ef9c2b6e1a")
        @JsonProperty("session_id")
        String sessionId,

        @NotNull(message = "is_phishing je povinné")
        @Schema(description = "Odpověď hráče, zda je zpráva phishing", example = "false")
        @JsonProperty("is_phishing")
        Boolean phishing,

        @NotNull(message = "remain_time je povinné")
        @Min(value = 0, message = "remain_time musí být >= 0")
        @Max(value = 60, message = "remain_time musí být <= 60")
        @Schema(description = "Počet zbývajících sekund", example = "10")
        @JsonProperty("remain_time")
        Integer remainTime
) {
}
