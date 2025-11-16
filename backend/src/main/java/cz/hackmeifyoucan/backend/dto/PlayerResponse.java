/*
 * PlayerResponse - data, která odesíláme zpět klientovi (frontendu).
 * DTO (Data Transfer Object) = objekt pouze pro přenos dat mezi vrstvami.
*/

package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PlayerResponse {

    @Schema(description = "Unikátní ID hráče", example = "1")
    private Long playerId;
    
    @Schema(description = "Hráčova unikátní přezdívka", example = "Player123")
    private String nickname;
    
    @Schema(description = "Hráčovo aktuální skóre", example = "100")
    private Integer score;

    // Konstruktor - vytvoří nový objekt PlayerResponse se všemi daty
    public PlayerResponse(Long playerId, String nickname, Integer score) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.score = score;
    }

    // Gettery - umožňují číst data z objektu
    public Long getPlayerId() {
        return playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getScore() {
        return score;
    }
}


