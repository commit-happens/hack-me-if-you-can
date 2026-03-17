package cz.hackmeifyoucan.backend.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cz.hackmeifyoucan.backend.dto.PlayerRequest;
import cz.hackmeifyoucan.backend.dto.PlayerUpdateRequest;
import cz.hackmeifyoucan.backend.dto.PlayerResponse;
import cz.hackmeifyoucan.backend.entity.Player;
import cz.hackmeifyoucan.backend.exception.PlayerNotFoundException;
import cz.hackmeifyoucan.backend.exception.DuplicateNicknameException;
import cz.hackmeifyoucan.backend.repository.PlayerRepository;
import cz.hackmeifyoucan.backend.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public PlayerResponse addPlayer(PlayerRequest playerRequest) {
        if (playerRepository.existsByNickname(playerRequest.nickname())) {
            throw new DuplicateNicknameException(playerRequest.nickname());
        }

        Player player = new Player();
        player.setNickname(playerRequest.nickname());

        if (playerRequest.score() != null) {
            player.setScore(playerRequest.score());
        } else {
            player.setScore(200);
        }
        
        Player savedPlayer = playerRepository.save(player);
        
        return convertToResponse(savedPlayer);
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Override
    @SuppressWarnings("null")
    public PlayerResponse getPlayerById(Long playerId) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        
        if (optionalPlayer.isEmpty()) {
            throw new PlayerNotFoundException(playerId);
        }
        
        Player player = optionalPlayer.get();
        return convertToResponse(player);
    }


    /* --------------------------------------------------------------------------------------------------- */
    @Override
    public List<PlayerResponse> getPlayers() {
        List<PlayerResponse> responseList = new ArrayList<>();
        
        Iterable<Player> allPlayers = playerRepository.findAll();
        
        for (Player player : allPlayers) {
            PlayerResponse response = convertToResponse(player);
            responseList.add(response);
        }
        
        return responseList;
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Override
    @SuppressWarnings("null")
    public PlayerResponse updatePlayer(Long playerId, PlayerUpdateRequest request) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        
        if (optionalPlayer.isEmpty()) {
            throw new PlayerNotFoundException(playerId);
        }
        
        Player player = optionalPlayer.get();
        
        if (request.nickname() != null && !request.nickname().isBlank()) {
            if (!request.nickname().equals(player.getNickname()) && playerRepository.existsByNickname(request.nickname())) {
                throw new DuplicateNicknameException(request.nickname());
            }
            player.setNickname(request.nickname());
        }
        if (request.score() != null) {
            player.setScore(request.score());
        }
        
        Player updatedPlayer = playerRepository.save(player);
        return convertToResponse(updatedPlayer);
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Override
    @SuppressWarnings("null")
    public PlayerResponse deletePlayer(Long playerId) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        
        if (optionalPlayer.isEmpty()) {
            return null;
        }

        Player player = optionalPlayer.get();
        playerRepository.deleteById(playerId);
        
        return convertToResponse(player);
    }
    
    /* --------------------------------------------------------------------------------------------------- */
    private PlayerResponse convertToResponse(Player player) {
        return new PlayerResponse(
            player.getId(),
            player.getNickname(),
            player.getScore()
        );
    }

}