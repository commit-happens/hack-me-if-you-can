/**
 * Error409Response - DTO pro 409 Conflict chyby (např. duplicitní přezdívka).
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "409 Conflict - konflikt s existujícími daty")
public record Error409Response(
    @Schema(description = "HTTP status kód", example = "409")
    Integer status,

    @Schema(description = "Popis chyby", example = "Porušení databázového omezení (např. unikátní přezdívka)")
    String error
) {}

