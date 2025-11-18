/*
 * PlayerResponse - data, která odesíláme zpět klientovi (frontendu).
 * DTO (Data Transfer Object) = objekt pouze pro přenos dat mezi vrstvami.
*/

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// Definice záznamu PlayerResponse s anotacemi pro dokumentaci API (obsahuje konstruktor a gettery)
public record PlayerResponse (
    @Schema(description = "Unikátní ID hráče", example = "1")
    Long playerId,
    
    @Schema(description = "Hráčova unikátní přezdívka", example = "Player123")
    String nickname,

    @Schema(description = "Hráčovo aktuální skóre", example = "100")
    Integer score
){
}


