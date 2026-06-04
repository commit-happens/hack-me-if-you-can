/**
 * Error500Response - DTO pro 500 Internal Server Error chyby.
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "500 Internal Server Error - neočekávaná chyba serveru")
public record Error500Response(
        @Schema(description = "HTTP status kód", example = "500")
        Integer status,

        @Schema(description = "Popis chyby", example = "Došlo k interní chybě serveru")
        String error
) {}