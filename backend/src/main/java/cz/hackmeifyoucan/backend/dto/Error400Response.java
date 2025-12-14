/**
 * Error400Response - DTO pro 400 Bad Request validační chyby s detaily polí.
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "400 Bad Request - validační chyba s detaily pro jednotlivá pole")
public class Error400Response {

    @Schema(description = "HTTP status kód", example = "400")
    private Integer status;

    @Schema(description = "Popis chyby", example = "Neplatná data v požadavku")
    private String error;

    @Schema(description = "Detaily chyb pro jednotlivá pole",
            example = "{\"nickname\": \"velikost musí být mezi 3 a 50\", \"score\": \"musí být větší než nebo rovno 0\"}")
    private Map<String, String> fields;
}
