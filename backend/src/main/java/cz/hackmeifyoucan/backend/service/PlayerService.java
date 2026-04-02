package cz.hackmeifyoucan.backend.service;

import java.util.List;

import cz.hackmeifyoucan.backend.dto.PlayerRequest;
import cz.hackmeifyoucan.backend.dto.PlayerResponse;
import cz.hackmeifyoucan.backend.dto.PlayerSummaryResponse;
import cz.hackmeifyoucan.backend.dto.PlayerUpdateRequest;

public interface PlayerService {

    List<PlayerResponse> getPlayers();

    PlayerResponse getPlayerById(Long playerId);
    
    PlayerResponse addPlayer(PlayerRequest request);
    
    PlayerResponse updatePlayer(Long playerId, PlayerUpdateRequest request);

    PlayerSummaryResponse getPlayerSummary(Long playerId, String sessionId);

}