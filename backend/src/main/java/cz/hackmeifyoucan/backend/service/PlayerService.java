package cz.hackmeifyoucan.backend.service;

import java.util.List;

import cz.hackmeifyoucan.backend.dto.PlayerRequest;
import cz.hackmeifyoucan.backend.dto.PlayerResponse;
import cz.hackmeifyoucan.backend.dto.PlayerSummaryResponse;

public interface PlayerService {

    List<PlayerResponse> getPlayers();

    PlayerResponse getPlayerById(Long playerId);

    PlayerResponse addPlayer(PlayerRequest request);

    PlayerSummaryResponse getPlayerSummary(Long playerId, String sessionId);

}