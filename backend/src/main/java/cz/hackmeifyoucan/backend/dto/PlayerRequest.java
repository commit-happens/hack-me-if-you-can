/**
 * PlayerRequest - data, která přijdou z frontendu pro vytvoření nebo aktualizaci hráče.
 * DTO (Data Transfer Object) = objekt pouze pro přenos dat.
 */

package cz.hackmeifyoucan.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlayerRequest(

    @NotBlank(message = "Přezdívka je povinná")
    @Size(min = 3, max = 50, message = "Přezdívka musí mít mezi 3 a 50 znaky")
    @Schema(description = "Unikátní přezdívka pro nového hráče", example = "Player123")
    String nickname,

    @Min(value = 0, message = "Skóre nemůže být záporné")
    @Schema(description = "Počáteční skóre (volitelné, výchozí je 100)", example = "100")
    Integer score
) {

}
