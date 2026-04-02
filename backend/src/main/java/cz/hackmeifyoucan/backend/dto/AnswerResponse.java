package cz.hackmeifyoucan.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record AnswerResponse(
        @Schema(description = "Výsledek vyhodnocení odpovědi", example = "true")
        @JsonProperty("answer_correct")
        boolean answerCorrect,

        @Schema(description = "Aktualizované skóre hráče", example = "395")
        int score
) {
}

