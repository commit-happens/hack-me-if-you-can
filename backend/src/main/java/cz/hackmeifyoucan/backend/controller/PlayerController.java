package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.model.Player;
import cz.hackmeifyoucan.backend.repository.PlayerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerRepository repo;

    public PlayerController(PlayerRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Player> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Player create(@RequestBody Player player) {
        return repo.save(player);
    }
}
