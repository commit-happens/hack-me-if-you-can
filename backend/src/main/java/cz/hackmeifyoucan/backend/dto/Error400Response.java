/**
 * Error400Response - DTO pro 400 Bad Request validační chyby s detaily polí.
 */

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "400 Bad Request - validační chyba s detaily pro jednotlivá pole")
public class Error400Response {

    @Schema(description = "HTTP status kód", example = "400")
    private Integer status;

    @Schema(description = "Popis chyby", example = "Neplatná data v požadavku")
    private String error;

    @Schema(description = "Detaily chyb pro jednotlivá pole", 
            example = "{\"nickname\": \"velikost musí být mezi 3 a 50\", \"score\": \"musí být větší než nebo rovno 0\"}")
    private Map<String, String> fields;

    // Konstruktory
    public Error400Response() {
    }

    public Error400Response(Integer status, String error, Map<String, String> fields) {
        this.status = status;
        this.error = error;
        this.fields = fields;
    }

    // Gettery
    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    // Settery
    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
