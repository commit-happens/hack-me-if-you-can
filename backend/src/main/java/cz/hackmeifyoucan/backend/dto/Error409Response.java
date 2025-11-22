/**
 * Error409Response - DTO pro 409 Conflict chyby (např. duplicitní přezdívka).
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "409 Conflict - konflikt s existujícími daty")
public class Error409Response {

    @Schema(description = "HTTP status kód", example = "409")
    private Integer status;

    @Schema(description = "Popis chyby", example = "Porušení databázového omezení (např. unikátní přezdívka)")
    private String error;

    // Konstruktory
    public Error409Response() {
    }

    public Error409Response(Integer status, String error) {
        this.status = status;
        this.error = error;
    }

    // Gettery
    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    // Settery
    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }
}
