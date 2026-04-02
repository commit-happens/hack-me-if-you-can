package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.PlayerSummaryResponse;
import cz.hackmeifyoucan.backend.entity.Player;
import cz.hackmeifyoucan.backend.repository.AnswerRepository;
import cz.hackmeifyoucan.backend.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    void given_missing_session_id_when_getting_summary_then_should_return_persisted_player_score() {
        Player player = Player.builder().id(1L).nickname("tester").score(3000).build();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        PlayerSummaryResponse response = playerService.getPlayerSummary(1L, null);

        assertThat(response.playerId()).isEqualTo(1L);
        assertThat(response.sessionId()).isNull();
        assertThat(response.score()).isEqualTo(3000);
        assertThat(response.potentialScore()).isEqualTo(3000);
        verify(answerRepository, never()).sumEarnedPointsByPlayerAndSession(1L, null);
        verify(answerRepository, never()).sumPotentialPointsForWrongAnswersInSession(1L, null);
    }

    @Test
    void given_blank_session_id_when_getting_summary_then_should_return_persisted_player_score() {
        Player player = Player.builder().id(1L).nickname("tester").score(2750).build();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        PlayerSummaryResponse response = playerService.getPlayerSummary(1L, "   ");

        assertThat(response.playerId()).isEqualTo(1L);
        assertThat(response.sessionId()).isNull();
        assertThat(response.score()).isEqualTo(2750);
        assertThat(response.potentialScore()).isEqualTo(2750);
    }

    @Test
    void given_session_id_when_getting_summary_then_should_return_session_calculated_score() {
        Player player = Player.builder().id(1L).nickname("tester").score(3000).build();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(answerRepository.sumEarnedPointsByPlayerAndSession(1L, "session-1")).thenReturn(400);
        when(answerRepository.sumPotentialPointsForWrongAnswersInSession(1L, "session-1")).thenReturn(150);

        PlayerSummaryResponse response = playerService.getPlayerSummary(1L, "session-1");

        assertThat(response.playerId()).isEqualTo(1L);
        assertThat(response.sessionId()).isEqualTo("session-1");
        assertThat(response.score()).isEqualTo(600);
        assertThat(response.potentialScore()).isEqualTo(750);
    }
}
