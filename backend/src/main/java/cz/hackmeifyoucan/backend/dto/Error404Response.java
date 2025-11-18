/**
 * Error404Response - DTO pro 404 Not Found chyby.
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "404 Not Found - požadovaný zdroj neexistuje")
public class Error404Response {

    @Schema(description = "HTTP status kód", example = "404")
    private Integer status;

    @Schema(description = "Popis chyby", example = "Hráč nenalezen pro ID: 1")
    private String error;

    // Konstruktory
    public Error404Response() {
    }

    public Error404Response(Integer status, String error) {
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
