/**
 * PlayerServiceImpl - hlavní logika pro práci s hráči. Prostředník mezi Controllerem a Repository. 
 * - Controller (přijímá HTTP požadavky)
 * - Repository (komunikuje s databází)
 */

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

    // Repository pro přístup k databázi
    private final PlayerRepository playerRepository;

    // Konstruktor - Spring automaticky předá PlayerRepository (dependency injection)
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public PlayerResponse addPlayer(PlayerRequest playerRequest) {
        // Unikátnost přezdívky
        if (playerRepository.existsByNickname(playerRequest.nickname())) {
            throw new DuplicateNicknameException(playerRequest.nickname());
        }

        Player player = new Player();
        player.setNickname(playerRequest.nickname());
        
        // Pokud frontend poslal skóre, použijeme ho, jinak nastavíme 100
        if (playerRequest.score() != null) {
            player.setScore(playerRequest.score());
        } else {
            player.setScore(100);  // Výchozí skóre
        }
        
        // Uložíme hráče do databáze (databáze automaticky vygeneruje ID)
        Player savedPlayer = playerRepository.save(player);
        
        // Převedeme databázový objekt na odpověď pro frontend
        return convertToResponse(savedPlayer);
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Override
    public PlayerResponse getPlayerById(Long playerId) {
        // vrací Optional<Player>
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        
        // Pokud hráč neexistuje, vyhodíme chybu (vrátí se HTTP 404)
        if (optionalPlayer.isEmpty()) {
            throw new PlayerNotFoundException(playerId);
        }
        
        // Hráč existuje - vrátíme ho jako response
        Player player = optionalPlayer.get();
        return convertToResponse(player);
    }


    /* --------------------------------------------------------------------------------------------------- */
    @Override
    public List<PlayerResponse> getPlayers() {
        // Vytvoříme prázdný seznam pro výsledky
        List<PlayerResponse> responseList = new ArrayList<>();
        
        // Získáme všechny hráče z databáze
        Iterable<Player> allPlayers = playerRepository.findAll();
        
        // Pro každého hráče vytvoříme response a přidáme do seznamu
        for (Player player : allPlayers) {
            PlayerResponse response = convertToResponse(player);
            responseList.add(response);
        }
        
        return responseList;
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Override
    public PlayerResponse updatePlayer(Long playerId, PlayerUpdateRequest request) {
        // Najdeme hráče v databázi
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        
        if (optionalPlayer.isEmpty()) {
            throw new PlayerNotFoundException(playerId);
        }
        
        Player player = optionalPlayer.get();
        
        // Aktualizujeme pouze pole, která frontend poslal
        if (request.nickname() != null && !request.nickname().isBlank()) {
            // Pokud se mění přezdívka, ověřit unikátnost (neporovnávat sám se sebou)
            if (!request.nickname().equals(player.getNickname()) && playerRepository.existsByNickname(request.nickname())) {
                throw new DuplicateNicknameException(request.nickname());
            }
            player.setNickname(request.nickname());
        }
        if (request.score() != null) {
            player.setScore(request.score());
        }
        
        // Uložíme změny do databáze
        Player updatedPlayer = playerRepository.save(player);
        
        return convertToResponse(updatedPlayer);
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Override
    public PlayerResponse deletePlayer(Long playerId) {
        // Zkusíme najít hráče
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        
        if (optionalPlayer.isEmpty()) {
            // Hráč neexistuje - vrátíme null (controller to převede na 204 No Content)
            return null;
        }
        
        // Hráč existuje - smažeme ho
        Player player = optionalPlayer.get();
        playerRepository.deleteById(playerId);
        
        // Vrátíme data smazaného hráče
        return convertToResponse(player);
    }
    
    /* --------------------------------------------------------------------------------------------------- */
    private PlayerResponse convertToResponse(Player player) {
        return new PlayerResponse(
            player.getPlayerId(),
            player.getNickname(),
            player.getScore()
        );
    }

}