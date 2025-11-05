/* 
This file contains the actual implementation of your PlayerService interface — 
it’s where the business logic really happens */

package cz.hackmeifyoucan.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.hackmeifyoucan.backend.entity.Player;
import cz.hackmeifyoucan.backend.repository.PlayerRepository;
import cz.hackmeifyoucan.backend.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public List<Player> getPlayers() {
        return (List<Player>) playerRepository.findAll();
    }

    @Override
    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId).get();
    }

    @Override
    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Long playerId, Player player) {
        Player existingPlayer = getPlayerById(playerId);
        
        if (player.getNickname() != null) {
            existingPlayer.setNickname(player.getNickname());
        }
        if (player.getScore() != null) {
            existingPlayer.setScore(player.getScore());
        }
        
        if (existingPlayer == null) {
            throw new IllegalStateException("Player should not be null at this point");
        }
        return playerRepository.save(existingPlayer);
    }

    @Override
    public Player deletePlayer(Long playerId) {
        Player player = getPlayerById(playerId);
        playerRepository.deleteById(playerId);
        return player;
    }

}
