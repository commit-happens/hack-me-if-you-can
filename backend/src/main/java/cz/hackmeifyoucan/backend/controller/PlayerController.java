/**
 * PlayerController - HTTP rozhraní pro práci s hráči (přijímá HTTP požadavky z frontendu a vrátí HTTP odpovědi).
 * Všechny endpointy začínají na /api/players
 */

package cz.hackmeifyoucan.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cz.hackmeifyoucan.backend.dto.PlayerRequest;
import cz.hackmeifyoucan.backend.dto.PlayerResponse;
import cz.hackmeifyoucan.backend.service.PlayerService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/players")
@Tag(name = "player-controller", description = "Endpointy pro správu hráčů - vytváření, čtení, aktualizace a mazání")
public class PlayerController {

    // Service vrstva - obsahuje abstrakni definice method
    private final PlayerService playerService;

    // Konstruktor - Spring automaticky předá PlayerService (dependency injection)
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(
        summary = "Vytvořit nového hráče",
        description = "Vytvoří nového hráče s přezdívkou a volitelným skóre. Pokud skóre není zadáno, nastaví se výchozí hodnota. Vrací pouze tělo odpovědi."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hráč úspěšně vytvořen"),
        @ApiResponse(responseCode = "400", description = "Neplatná data (např. krátká přezdívka, záporné skóre)"),
        @ApiResponse(responseCode = "409", description = "Přezdívka již existuje")
    })
    @PostMapping
    public ResponseEntity<PlayerResponse> addPlayer(@RequestBody @Valid PlayerRequest playerRequest) {
        PlayerResponse playerResponse = playerService.addPlayer(playerRequest);
        return ResponseEntity.ok(playerResponse);
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Operation(summary = "Získat hráče podle ID", description = "Vrací údaje o jednom hráči na základě jeho unikátního ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hráč nalezen"),
        @ApiResponse(responseCode = "400", description = "Hráč s tímto ID neexistuje")
    })
    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable Long playerId) {
        PlayerResponse response = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(response);
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Operation(summary = "Získat všechny hráče", description = "Vrací seznam všech hráčů v databázi.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seznam hráčů úspěšně vrácen (může být prázdný)")
    })
    @GetMapping
    public ResponseEntity<List<PlayerResponse>> getPlayers() {
        List<PlayerResponse> players = playerService.getPlayers();
        return ResponseEntity.ok(players);
    }
    
    /* --------------------------------------------------------------------------------------------------- */
    @Operation(summary = "Aktualizovat hráče", description = "Částečně aktualizuje údaje hráče. Můžete poslat pouze pole, která chcete změnit (např. jen přezdívku nebo jen skóre).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hráč úspěšně aktualizován"),
        @ApiResponse(responseCode = "400", description = "Neplatná data nebo hráč neexistuje"),
        @ApiResponse(responseCode = "409", description = "Nová přezdívka již existuje")
    })
    @PatchMapping("/{playerId}")
    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable Long playerId, @RequestBody @Valid PlayerRequest request) {
        PlayerResponse response = playerService.updatePlayer(playerId, request);
        return ResponseEntity.ok(response);
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Operation(summary = "Smazat hráče", description = "Odstraní hráče z databáze. Operace je idempotentí - opakované volání nevyvolá chybu.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hráč úspěšně smazán, vrácena data smazaného hráče"),
        @ApiResponse(responseCode = "204", description = "Hráč neexistuje (již smazán nebo nikdy nebyl vytvořen)")
    })
    @DeleteMapping("/{playerId}")
    public ResponseEntity<PlayerResponse> deletePlayer(@PathVariable Long playerId) {
        PlayerResponse response = playerService.deletePlayer(playerId);
        
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(response);
    }
}