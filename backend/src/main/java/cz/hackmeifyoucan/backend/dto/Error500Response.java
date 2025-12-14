/**
 * Error500Response - DTO pro 500 Internal Server Error chyby.
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "500 Internal Server Error - neočekávaná chyba serveru")
public class Error500Response {

    @Schema(description = "HTTP status kód", example = "500")
    private Integer status;

    @Schema(description = "Popis chyby", example = "Došlo k interní chybě serveru")
    private String error;
}
