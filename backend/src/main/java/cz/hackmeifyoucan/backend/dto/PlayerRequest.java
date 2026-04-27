package cz.hackmeifyoucan.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlayerRequest(

    @NotBlank(message = "Přezdívka je povinná")
    @Size(min = 3, max = 50, message = "Přezdívka musí mít mezi 3 a 50 znaky")
    @Schema(description = "Unikátní přezdívka pro nového hráče", example = "Player123")
    String nickname
) {

}
