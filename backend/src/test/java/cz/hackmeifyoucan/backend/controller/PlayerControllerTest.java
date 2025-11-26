/*Testování všech REST Api pro Player - úspěšné i neúspěšné cesty.*/

package cz.hackmeifyoucan.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import cz.hackmeifyoucan.backend.dto.PlayerResponse;
import cz.hackmeifyoucan.backend.dto.PlayerRequest;
import cz.hackmeifyoucan.backend.exception.PlayerNotFoundException;
import cz.hackmeifyoucan.backend.service.PlayerService;


// 1. Tato anotace říká Springu: "Otestuj jen PlayerController"
@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    // Nástroj pro simulaci HTTP requestů (jako Postman v kódu)
    @Autowired
    private MockMvc mockMvc;

    // 2. Simulujeme Service, aby test nebyl závislý na databázi
    @MockitoBean
    private PlayerService playerService;

    // --- Sekce pro GET ALL ---
    @Nested
    class GetPlayersTests {
        @Test
        void shouldReturnAllPlayers() throws Exception {
            // Given
            PlayerResponse player1 = new PlayerResponse(1L, "David", 80);
            PlayerResponse player2 = new PlayerResponse(2L, "Petr", 90);
            List<PlayerResponse> mockPlayers = Arrays.asList(player1, player2);

            // Mock playerService
            when(playerService.getPlayers()).thenReturn(mockPlayers);

            // When
            mockMvc.perform(get("/api/players"))
                    // Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].nickname").value("David"))
                    .andExpect(jsonPath("$[1].playerId").value(2))
                    .andExpect(jsonPath("$[1].score").value(90))
                    ;
        }

        @Test
        void shouldReturnEmptyListWhenNoPlayersFound() throws Exception {
            // Given
            List<PlayerResponse> mockPlayers = Arrays.asList();

            // Mock playerService
            when(playerService.getPlayers()).thenReturn(mockPlayers);

            // When
            mockMvc.perform(get("/api/players"))
                    // Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.size()").value(0))
                    ;
        }

        @Test
        void shouldReturn500WhenServiceFails() throws Exception {
            // Given
            RuntimeException runtimeException = new RuntimeException("Chyba (např. databáze)");

            // Mock playerService
            when(playerService.getPlayers()).thenThrow(runtimeException);

            // When
            mockMvc.perform(get("/api/players")
                    // Then
                    .contentType("application/json"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error").value("Neočekávaná chyba serveru"))
                    ;
        }
    }

    // --- Sekce pro POST ---
    // addPlayer
    @Nested
    class AddPlayerTests {
        @Test
        void shouldAddPlayerSuccessfully() throws Exception {
            // Given
            PlayerRequest mockPlayerRequest = new PlayerRequest("Alice", 80);
            PlayerResponse mockPlayerResponse = new PlayerResponse(1L, "Alice", 80);

            // Mock
            when(playerService.addPlayer(mockPlayerRequest)).thenReturn(mockPlayerResponse);

            // When
            mockMvc.perform(
                    post("/api/players")
                    .content("{\"nickname\":\"Alice\",\"score\":80}")
                    .contentType("application/json")
            )
                    // Then        
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("Alice"))
                    .andExpect(jsonPath("$.score").value(80))

                    ;
        }

        @Test
        void shouldAddPlayerWithDefaultScoreWhenScoreNotProvided() throws Exception {
            // Given
            PlayerRequest mockPlayerRequest = new PlayerRequest("BobDefault", null);
            PlayerResponse mockPlayerResponse = new PlayerResponse(2L, "BobDefault", 100);

            // Mock
            when(playerService.addPlayer(mockPlayerRequest)).thenReturn(mockPlayerResponse);

            // When
            mockMvc.perform(
                    post("/api/players")
                    .content("{\"nickname\":\"BobDefault\"}")
                    .contentType("application/json")
            )
                    // Then        
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.playerId").value(2))
                    .andExpect(jsonPath("$.nickname").value("BobDefault"))
                    .andExpect(jsonPath("$.score").value(100));
        }
                    
        @ParameterizedTest
        @CsvSource(delimiterString = " | ", value = {
            "'{\"nickname\":\"AB\",\"score\":100}' | nickname | Přezdívka musí mít mezi 3 a 50 znaky",
            "'{\"nickname\":\"ValidName\",\"score\":-5}' | score | Skóre nemůže být záporné",
            "'{\"score\":100}' | nickname | Přezdívka je povinná"
        })
        void shouldReturn400WhenInvalidInput(String invalidJson, String fieldName, String expectedFieldError) throws Exception {
            // When
            mockMvc.perform(
                    post("/api/players")
                    .content(invalidJson)
                    .contentType("application/json")
            )
                    // Then        
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.error").value("Neplatná data v požadavku"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.fields." + fieldName).value(expectedFieldError))
                    ;
        }
    }

    // --- Sekce pro GET ONE ---
    // getPlayer
    @Nested
    class GetPlayerTests {
        @Test
        void shouldReturnPlayer() throws Exception {
            // Given
            PlayerResponse mockPlayer = new PlayerResponse(1L, "Alice", 100);
            
            // Mock playerService
            when(playerService.getPlayerById(1L)).thenReturn(mockPlayer);

            // When
            mockMvc.perform(get("/api/players/1"))
                    // Then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("Alice"))
                    .andExpect(jsonPath("$.score").value(100));
        }

        @Test
        void shouldReturn404WhenPlayerNotFound() throws Exception {
            // Given
            Long nonExistentPlayerId = 999L;
            PlayerNotFoundException playerNotFoundException = new PlayerNotFoundException(nonExistentPlayerId);

            // Mock playerService
            when(playerService.getPlayerById(nonExistentPlayerId)).thenThrow(playerNotFoundException);

            // When
            mockMvc.perform(get("/api/players/{playerId}", nonExistentPlayerId))
                    // Then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error").value("Hráč nenalezen pro ID: " + nonExistentPlayerId));
        }
    }

    // --- Sekce pro PATCH ---
    // updatePlayer
    @Nested
    class UpdatePlayerTests {
        @Test
        void shouldUpdatePlayerSuccessfully() throws Exception {
            // Given
            Long playerIdToUpdate = 1L;
            PlayerRequest mockUpdateRequest = new PlayerRequest("AliceUpdated", 120);
            PlayerResponse mockUpdatedResponse = new PlayerResponse(playerIdToUpdate, "AliceUpdated", 120);

            // Mock
            when(playerService.updatePlayer(playerIdToUpdate, mockUpdateRequest)).thenReturn(mockUpdatedResponse);

            // When
            mockMvc.perform(
                    patch("/api/players/{playerId}", playerIdToUpdate)
                    .content("{\"nickname\":\"AliceUpdated\",\"score\":120}")
                    .contentType("application/json")
            )
                    // Then        
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("AliceUpdated"))
                    .andExpect(jsonPath("$.score").value(120))
                    ;
        }

        @Test
        void shouldUpdatePlayerScoreSuccessfully() throws Exception {
            // Given
            Long playerIdToUpdate = 1L;
            PlayerRequest mockUpdateRequest = new PlayerRequest(null, 120);
            PlayerResponse mockUpdatedResponse = new PlayerResponse(playerIdToUpdate, "AliceUpdated", 120);

            // Mock
            when(playerService.updatePlayer(playerIdToUpdate, mockUpdateRequest)).thenReturn(mockUpdatedResponse);

            // When
            mockMvc.perform(
                    patch("/api/players/{playerId}", playerIdToUpdate)
                    .content("{\"nickname\":null,\"score\":120}")
                    .contentType("application/json")
            )
                    // Then        
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("AliceUpdated"))
                    .andExpect(jsonPath("$.score").value(120))
                    ;
        }
    }

    // --- Sekce pro DELETE ---
    // deletePlayer
}

// @Test
//     void shouldReturn404WhenPlayerNotFound() throws Exception {
//         Long nonExistentId = 999L;

//         // DŮLEŽITÉ: Zde musíte vyhodit PŘESNĚ tu výjimku, kterou chytá váš @ExceptionHandler
//         when(playerService.getPlayerById(nonExistentId))
//             .thenThrow(new PlayerNotFoundException("Hráč s ID " + nonExistentId + " neexistuje"));

//         mockMvc.perform(get("/api/players/{playerId}", nonExistentId))
//                 .andExpect(status().isNotFound()) // Nyní by to mělo projít (404)
                
//                 // Pokud vracíte Error404Response (jak máte v @ApiResponse), můžete otestovat i tělo:
//                 .andExpect(jsonPath("$.message").exists()); 
//     }