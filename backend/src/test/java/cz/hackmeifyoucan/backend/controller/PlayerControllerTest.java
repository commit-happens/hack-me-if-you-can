/*Testování všech REST Api pro Player - úspěšné i neúspěšné cesty.*/

package cz.hackmeifyoucan.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cz.hackmeifyoucan.backend.dto.PlayerRequest;
import cz.hackmeifyoucan.backend.dto.PlayerResponse;
import cz.hackmeifyoucan.backend.dto.PlayerUpdateRequest;
import cz.hackmeifyoucan.backend.exception.DuplicateNicknameException;
import cz.hackmeifyoucan.backend.exception.PlayerNotFoundException;
import cz.hackmeifyoucan.backend.service.PlayerService;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    private static final String PLAYERS_API = "/players";
    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @Nested
    class GetPlayersTests {

        @Test
        void given_players_exist_when_getting_all_players_then_return_all_players() throws Exception {
            // Given
            PlayerResponse player1 = new PlayerResponse(1L, "Terminátor", 80);
            PlayerResponse player2 = new PlayerResponse(2L, "T-1000", 90);
            List<PlayerResponse> players = Arrays.asList(player1, player2);
            when(playerService.getPlayers()).thenReturn(players);

            // When & Then
            mockMvc.perform(get(PLAYERS_API))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].nickname").value("Terminátor"))
                    .andExpect(jsonPath("$[0].playerId").value(1))
                    .andExpect(jsonPath("$[0].score").value(80))
                    .andExpect(jsonPath("$[1].nickname").value("T-1000"))
                    .andExpect(jsonPath("$[1].playerId").value(2))
                    .andExpect(jsonPath("$[1].score").value(90));
        }

        @Test
        void given_no_players_when_getting_all_players_then_return_empty_list() throws Exception {
            // Given
            when(playerService.getPlayers()).thenReturn(List.of());

            // When & Then
            mockMvc.perform(get(PLAYERS_API))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(0));
        }

        @Test
        void given_service_throws_exception_when_getting_players_then_return_500_error() throws Exception {
            // Given
            when(playerService.getPlayers()).thenThrow(new RuntimeException("Chyba (např. databáze)"));

            // When & Then
            mockMvc.perform(get(PLAYERS_API))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").value("Neočekávaná chyba serveru"));
        }
    }

    @Nested
    class AddPlayerTests {

        @Test
        void given_valid_player_request_when_adding_player_then_return_created_player() throws Exception {
            // Given
            PlayerRequest request = new PlayerRequest("Terminátor", 80);
            PlayerResponse response = new PlayerResponse(1L, "Terminátor", 80);
            when(playerService.addPlayer(request)).thenReturn(response);

            // When & Then
            mockMvc.perform(post(PLAYERS_API)
                            .contentType(APPLICATION_JSON)
                            .content("{\"nickname\":\"Terminátor\",\"score\":80}"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("Terminátor"))
                    .andExpect(jsonPath("$.score").value(80));
        }

        @Test
        void given_player_request_without_score_when_adding_player_then_assign_default_score() throws Exception {
            // Given
            PlayerRequest request = new PlayerRequest("Terminátor", null);
            PlayerResponse response = new PlayerResponse(2L, "Terminátor", 200);
            when(playerService.addPlayer(request)).thenReturn(response);

            // When & Then
            mockMvc.perform(post(PLAYERS_API)
                            .contentType(APPLICATION_JSON)
                            .content("{\"nickname\":\"Terminátor\"}"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.playerId").value(2))
                    .andExpect(jsonPath("$.nickname").value("Terminátor"))
                    .andExpect(jsonPath("$.score").value(200));
        }

        @SuppressWarnings("null")
        @ParameterizedTest
        @CsvSource(delimiterString = " | ", value = {
            "'{\"nickname\":\"AB\",\"score\":100}' | nickname | Přezdívka musí mít mezi 3 a 50 znaky",
            "'{\"nickname\":\"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"score\":100}' | nickname | Přezdívka musí mít mezi 3 a 50 znaky",
            "'{\"nickname\":\"ValidName\",\"score\":-5}' | score | Skóre nemůže být záporné",
            "'{\"score\":100}' | nickname | Přezdívka je povinná"
        })
        void given_invalid_player_data_when_adding_player_then_return_400_bad_request(String invalidJson, String fieldName, String expectedFieldError)
                throws Exception {
            // When & Then
            mockMvc.perform(post(PLAYERS_API)
                            .contentType(APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.error").value("Neplatná data v požadavku"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.fields." + fieldName).value(expectedFieldError));
        }

        @Test
        void given_duplicate_nickname_when_adding_player_then_return_409_conflict() throws Exception {
            // Given
            PlayerRequest request = new PlayerRequest("TerminátorJižExistuje", 80);
            when(playerService.addPlayer(request))
                    .thenThrow(new DuplicateNicknameException("TerminátorJižExistuje"));

            // When & Then
            mockMvc.perform(post(PLAYERS_API)
                            .contentType(APPLICATION_JSON)
                            .content("{\"nickname\":\"TerminátorJižExistuje\",\"score\":80}"))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.error").value("Přezdívka již existuje: TerminátorJižExistuje"))
                    .andExpect(jsonPath("$.fields.nickname").value("Přezdívka už je obsazená"));
        }

        @Test
        void given_service_throws_exception_when_adding_player_then_return_500_error() throws Exception {
            // Given
            PlayerRequest request = new PlayerRequest("Terminátor", 80);
            when(playerService.addPlayer(request)).thenThrow(new RuntimeException("Chyba (např. databáze)"));

            // When & Then
            mockMvc.perform(post(PLAYERS_API)
                            .contentType(APPLICATION_JSON)
                            .content("{\"nickname\":\"Terminátor\",\"score\":80}"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").value("Neočekávaná chyba serveru"));
        }
    }

    @Nested
    class GetPlayerTests {

        @Test
        void given_valid_player_id_when_getting_player_then_return_player() throws Exception {
            // Given
            PlayerResponse player = new PlayerResponse(1L, "Terminátor", 100);
            when(playerService.getPlayerById(1L)).thenReturn(player);

            // When & Then
            mockMvc.perform(get(PLAYERS_API + "/{playerId}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("Terminátor"))
                    .andExpect(jsonPath("$.score").value(100));
        }

        @ParameterizedTest
        @CsvSource({"1", "0", "-1", "9999999999"})
        void given_invalid_player_id_when_getting_player_then_return_404_not_found(Long playerId) throws Exception {
            // Given
            when(playerService.getPlayerById(playerId))
                    .thenThrow(new PlayerNotFoundException(playerId));

            // When & Then
            mockMvc.perform(get(PLAYERS_API + "/{playerId}", playerId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Hráč nenalezen pro ID: " + playerId));
        }

        @Test
        void given_service_throws_exception_when_getting_player_then_return_500_error() throws Exception {
            // Given
            when(playerService.getPlayerById(1L)).thenThrow(new RuntimeException("Chyba (např. databáze)"));

            // When & Then
            mockMvc.perform(get(PLAYERS_API + "/{playerId}", 1L))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").value("Neočekávaná chyba serveru"));
        }
    }

    @Nested
    class UpdatePlayerTests {

        @Test
        void given_valid_score_update_when_updating_player_then_return_updated_player() throws Exception {
            // Given
            PlayerUpdateRequest request = new PlayerUpdateRequest(null, 120);
            PlayerResponse response = new PlayerResponse(1L, "Terminátor", 120);
            when(playerService.updatePlayer(1L, request)).thenReturn(response);

            // When & Then
            mockMvc.perform(patch(PLAYERS_API + "/{playerId}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content("{\"score\":120}"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("Terminátor"))
                    .andExpect(jsonPath("$.score").value(120));
        }

        @Test
        void given_valid_nickname_update_when_updating_player_then_return_updated_player() throws Exception {
            // Given
            PlayerUpdateRequest request = new PlayerUpdateRequest("TerminátorAktualizován", null);
            PlayerResponse response = new PlayerResponse(1L, "TerminátorAktualizován", 100);
            when(playerService.updatePlayer(1L, request)).thenReturn(response);

            // When & Then
            mockMvc.perform(patch(PLAYERS_API + "/1")
                            .contentType(APPLICATION_JSON)
                            .content("{\"nickname\":\"TerminátorAktualizován\"}"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("TerminátorAktualizován"))
                    .andExpect(jsonPath("$.score").value(100));
        }

        @Test
        void given_valid_nickname_and_score_update_when_updating_player_then_return_updated_player() throws Exception {
            // Given
            PlayerUpdateRequest request = new PlayerUpdateRequest("TerminátorAktualizován", 120);
            PlayerResponse response = new PlayerResponse(1L, "TerminátorAktualizován", 120);
            when(playerService.updatePlayer(1L, request)).thenReturn(response);

            // When & Then
            mockMvc.perform(patch(PLAYERS_API + "/1")
                            .contentType(APPLICATION_JSON)
                            .content("{\"nickname\":\"TerminátorAktualizován\", \"score\":120}"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("TerminátorAktualizován"))
                    .andExpect(jsonPath("$.score").value(120));
        }

        @SuppressWarnings("null")
        @ParameterizedTest
        @CsvSource(delimiterString = " | ", value = {
            "'{\"nickname\":\"AB\"}' | nickname | Přezdívka musí mít mezi 3 a 50 znaky",
            "'{\"nickname\":\"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"}' | nickname | Přezdívka musí mít mezi 3 a 50 znaky",
            "'{\"score\":-5}' | score | Skóre nemůže být záporné"
        })
        void given_invalid_player_update_data_when_updating_player_then_return_400_bad_request(String invalidJson, String fieldName, String expectedFieldError)
                throws Exception {
            // When & Then
            mockMvc.perform(patch(PLAYERS_API + "/1")
                            .contentType(APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.error").value("Neplatná data v požadavku"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.fields." + fieldName).value(expectedFieldError));
        }

        @ParameterizedTest
        @CsvSource({"1", "0", "-1", "9999999999"})
        void given_invalid_player_id_when_updating_player_then_return_404_not_found(Long playerId) throws Exception {
            // Given
            PlayerUpdateRequest request = new PlayerUpdateRequest("TerminátorAktualizován", 100);
            when(playerService.updatePlayer(playerId, request))
                    .thenThrow(new PlayerNotFoundException(playerId));

            // When & Then
            mockMvc.perform(patch(PLAYERS_API + "/{playerId}", playerId)
                            .contentType(APPLICATION_JSON)
                            .content("{\"nickname\":\"TerminátorAktualizován\",\"score\":100}"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Hráč nenalezen pro ID: " + playerId));
        }

        @Test
        void given_duplicate_nickname_when_updating_player_then_return_409_conflict() throws Exception {
            // Given
            PlayerUpdateRequest request = new PlayerUpdateRequest("TerminátorJižExistuje", null);
            when(playerService.updatePlayer(1L, request))
                    .thenThrow(new DuplicateNicknameException("TerminátorJižExistuje"));

            // When & Then
            mockMvc.perform(patch(PLAYERS_API + "/1")
                            .contentType(APPLICATION_JSON)
                            .content("{\"nickname\":\"TerminátorJižExistuje\"}"))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.error").value("Přezdívka již existuje: TerminátorJižExistuje"))
                    .andExpect(jsonPath("$.fields.nickname").value("Přezdívka už je obsazená"));
        }

        @Test
        void given_service_throws_exception_when_updating_player_then_return_500_error() throws Exception {
            // Given
            PlayerUpdateRequest request = new PlayerUpdateRequest("Terminátor", 100);
            when(playerService.updatePlayer(1L, request)).thenThrow(new RuntimeException("Chyba (např. databáze)"));

            // When & Then
            mockMvc.perform(patch(PLAYERS_API + "/1")
                            .contentType(APPLICATION_JSON)
                            .content("{\"nickname\":\"Terminátor\",\"score\":100}"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").value("Neočekávaná chyba serveru"));
        }
    }

    @Nested
    class DeletePlayerTests {

        @Test
        void given_valid_player_id_when_deleting_player_then_return_deleted_player() throws Exception {
            // Given
            PlayerResponse response = new PlayerResponse(1L, "Terminátor", 100);
            when(playerService.deletePlayer(1L)).thenReturn(response);

            // When & Then
            mockMvc.perform(delete(PLAYERS_API + "/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.playerId").value(1))
                    .andExpect(jsonPath("$.nickname").value("Terminátor"))
                    .andExpect(jsonPath("$.score").value(100));
        }

        @ParameterizedTest
        @CsvSource({"9999999999", "0", "-1"})
        void given_invalid_player_id_when_deleting_player_then_return_204_no_content(Long playerId) throws Exception {
            // Given
            when(playerService.deletePlayer(playerId)).thenReturn(null);

            // When & Then
            mockMvc.perform(delete(PLAYERS_API + "/{playerId}", playerId))
                    .andExpect(status().isNoContent());
        }

        @Test
        void given_service_throws_exception_when_deleting_player_then_return_500_error() throws Exception {
            // Given
            when(playerService.deletePlayer(1L)).thenThrow(new RuntimeException("Chyba (např. databáze)"));

            // When & Then
            mockMvc.perform(delete(PLAYERS_API + "/1"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").value("Neočekávaná chyba serveru"));
        }
    }
}