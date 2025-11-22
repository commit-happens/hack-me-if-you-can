/* Rozhraní služby popisující business logiku mezi controllery (API) a repository (databází) */

package cz.hackmeifyoucan.backend.service;

import java.util.List;

import cz.hackmeifyoucan.backend.dto.PlayerRequest;
import cz.hackmeifyoucan.backend.dto.PlayerUpdateRequest;
import cz.hackmeifyoucan.backend.dto.PlayerResponse;

public interface PlayerService {

    List<PlayerResponse> getPlayers();

    PlayerResponse getPlayerById(Long playerId);
    
    PlayerResponse addPlayer(PlayerRequest request);
    
    PlayerResponse updatePlayer(Long playerId, PlayerUpdateRequest request);
    
    PlayerResponse deletePlayer(Long playerId);

}