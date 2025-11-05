/* This file defines a REST controller — the part of the backend 
that handles HTTP requests coming from the frontend (or any client). */

package cz.hackmeifyoucan.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import cz.hackmeifyoucan.backend.entity.Player;
import cz.hackmeifyoucan.backend.service.PlayerService;


@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayers() {
        return ResponseEntity.ok(playerService.getPlayers());
    }
    
    @GetMapping("/players/{playerId}")
    public ResponseEntity<Player> getPlayer(@PathVariable("playerId") Long playerId) {
        return ResponseEntity.ok(playerService.getPlayerById(playerId));
    }

    @PostMapping("/players")
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        return ResponseEntity.ok(playerService.addPlayer(player));
    }

    @PatchMapping("/players/{playerId}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("playerId") Long playerId, @RequestBody Player player) {
        return ResponseEntity.ok(playerService.updatePlayer(playerId, player));
    }

    @DeleteMapping("/players/{playerId}")
    public ResponseEntity<Player> deletePlayer(@PathVariable("playerId") Long playerId) {
        Player deletedPlayer = playerService.getPlayerById(playerId);
        playerService.deletePlayer(playerId);
        return ResponseEntity.ok(deletedPlayer);
    }
}
