/**
 * Error409Response - DTO pro 409 Conflict chyby (např. duplicitní přezdívka).
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "409 Conflict - konflikt s existujícími daty")
public class Error409Response {

    @Schema(description = "HTTP status kód", example = "409")
    private Integer status;

    @Schema(description = "Popis chyby", example = "Porušení databázového omezení (např. unikátní přezdívka)")
    private String error;
}
