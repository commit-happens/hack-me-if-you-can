/**
 * Error404Response - DTO pro 404 Not Found chyby.
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "404 Not Found - požadovaný zdroj neexistuje")
public class Error404Response {

    @Schema(description = "HTTP status kód", example = "404")
    private Integer status;

    @Schema(description = "Popis chyby", example = "Požadovaný zdroj nebyl nalezen")
    private String error;
}
