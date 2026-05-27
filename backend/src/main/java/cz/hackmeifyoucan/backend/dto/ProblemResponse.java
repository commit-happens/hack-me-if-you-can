package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProblemResponse", description = "Chyták identifikovaný v obsahu phishingové zprávy")
public record ProblemResponse(

        @Schema(description = "Identifikátor chytáku (odpovídá markeru v obsahu zprávy)", example = "time-pressure")
        String id,

        @Schema(description = "Popis chytáku zobrazený uživateli po zodpovězení otázky", example = "Uměle vytvářený tlak časovým limitem je typický znak phishingu.")
        String description
) {}
