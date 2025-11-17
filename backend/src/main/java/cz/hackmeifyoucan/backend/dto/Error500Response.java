/**
 * Error500Response - DTO pro 500 Internal Server Error chyby.
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "500 Internal Server Error - neočekávaná chyba serveru")
public class Error500Response {

    @Schema(description = "HTTP status kód", example = "500")
    private Integer status;

    @Schema(description = "Popis chyby", example = "Došlo k interní chybě serveru")
    private String error;

    // Konstruktory
    public Error500Response() {
    }

    public Error500Response(Integer status, String error) {
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
