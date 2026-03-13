package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlayerResponse (
    @Schema(description = "Unikátní ID hráče", example = "1")
    Long playerId,
    
    @Schema(description = "Hráčova unikátní přezdívka", example = "Player123")
    String nickname,

    @Schema(description = "Hráčovo aktuální skóre", example = "100")
    Integer score
){
}


