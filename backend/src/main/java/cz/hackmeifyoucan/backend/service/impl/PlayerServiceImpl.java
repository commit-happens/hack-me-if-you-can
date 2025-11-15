/* Implementace rozhraní PlayerService — zde probíhá business logika */

package cz.hackmeifyoucan.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cz.hackmeifyoucan.backend.entity.Player;
import cz.hackmeifyoucan.backend.repository.PlayerRepository;
import cz.hackmeifyoucan.backend.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    // Konstruktor pro injekci závislosti (doporučeno místo field injection)
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> getPlayers() {
        return (List<Player>) playerRepository.findAll();
    }

    @Override
    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Hráč nenalezen pro ID: " + playerId + ". Načtení selhalo."));
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
        return playerRepository.save(existingPlayer);
    }

    @Override
    public Player deletePlayer(Long playerId) {
        Player player = getPlayerById(playerId);
        playerRepository.deleteById(playerId);
        return player;
    }

}