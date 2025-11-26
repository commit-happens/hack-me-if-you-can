/**
 * PlayerUpdateRequest - DTO pro částečnou (PATCH) aktualizaci hráče.
 * Všechna pole jsou VOLITELNÁ. Pokud pole nepřijde v JSONu, nemění se.
 */
package cz.hackmeifyoucan.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record PlayerUpdateRequest(

    @Size(min = 3, max = 50, message = "Přezdívka musí mít mezi 3 a 50 znaky")
    @Schema(description = "Nová přezdívka (volitelné)", example = "NewNick")
    String nickname,

    @Min(value = 0, message = "Skóre nemůže být záporné")
    @Schema(description = "Nové skóre (volitelné)", example = "95")
    Integer score
) {}
