/**
 * Error404Response - DTO pro 404 Not Found chyby.
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "404 Not Found - požadovaný zdroj neexistuje")
public record Error404Response(
    @Schema(description = "HTTP status kód", example = "404")
    Integer status,

    @Schema(description = "Popis chyby", example = "Požadovaný zdroj nebyl nalezen")
    String error
) {}
