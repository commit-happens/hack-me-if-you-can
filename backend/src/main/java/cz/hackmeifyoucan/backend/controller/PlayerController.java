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
import cz.hackmeifyoucan.backend.dto.PlayerUpdateRequest;
import cz.hackmeifyoucan.backend.dto.PlayerResponse;
import cz.hackmeifyoucan.backend.service.PlayerService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import cz.hackmeifyoucan.backend.dto.Error400Response;
import cz.hackmeifyoucan.backend.dto.Error404Response;
import cz.hackmeifyoucan.backend.dto.Error409Response;

@RestController
@RequestMapping("/players")
@Tag(name = "player-controller", description = "Endpointy pro správu hráčů - vytváření, čtení, aktualizace a mazání")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(
        summary = "Vytvořit nového hráče",
        description = "Vytvoří nového hráče s přezdívkou a volitelným skóre. Pokud skóre není zadáno, nastaví se výchozí hodnota. Vrací pouze tělo odpovědi."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hráč úspěšně vytvořen",
                     content = @Content(schema = @Schema(implementation = PlayerResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validační chyba - neplatná data (např. krátká přezdívka, záporné skóre)",
                     content = @Content(schema = @Schema(implementation = Error400Response.class))),
        @ApiResponse(responseCode = "409", description = "Konflikt - přezdívka již existuje",
                     content = @Content(schema = @Schema(implementation = Error409Response.class)))
    })
    @PostMapping
    public ResponseEntity<PlayerResponse> addPlayer(@RequestBody @Valid PlayerRequest playerRequest) {
        PlayerResponse playerResponse = playerService.addPlayer(playerRequest);
        return ResponseEntity.ok(playerResponse);
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Operation(summary = "Získat hráče podle ID", description = "Vrací údaje o jednom hráči na základě jeho unikátního ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hráč nalezen",
                     content = @Content(schema = @Schema(implementation = PlayerResponse.class))),
        @ApiResponse(responseCode = "404", description = "Hráč s tímto ID neexistuje",
                     content = @Content(schema = @Schema(implementation = Error404Response.class)))
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
        @ApiResponse(responseCode = "200", description = "Hráč úspěšně aktualizován",
                     content = @Content(schema = @Schema(implementation = PlayerResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validační chyba - neplatná data",
                     content = @Content(schema = @Schema(implementation = Error400Response.class))),
        @ApiResponse(responseCode = "404", description = "Nenalezeno - hráč s tímto ID neexistuje",
                     content = @Content(schema = @Schema(implementation = Error404Response.class))),
        @ApiResponse(responseCode = "409", description = "Konflikt - nová přezdívka již existuje",
                     content = @Content(schema = @Schema(implementation = Error409Response.class)))
    })
    @PatchMapping("/{playerId}")
    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable Long playerId, @RequestBody @Valid PlayerUpdateRequest request) {
        PlayerResponse response = playerService.updatePlayer(playerId, request);
        return ResponseEntity.ok(response);
    }

    /* --------------------------------------------------------------------------------------------------- */
    @Operation(summary = "Smazat hráče", description = "Odstraní hráče z databáze. Operace je idempotentí - opakované volání nevyvolá chybu.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hráč úspěšně smazán, vrácena data smazaného hráče",
                     content = @Content(schema = @Schema(implementation = PlayerResponse.class))),
        @ApiResponse(responseCode = "204", description = "Hráč neexistuje (již smazán nebo nikdy nebyl vytvořen)", 
                     content = @Content())
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