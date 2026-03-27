package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.AnswerRequest;
import cz.hackmeifyoucan.backend.dto.AnswerResponse;
import cz.hackmeifyoucan.backend.entity.AnswerId;
import cz.hackmeifyoucan.backend.entity.PhishingCategory;
import cz.hackmeifyoucan.backend.entity.Player;
import cz.hackmeifyoucan.backend.entity.Question;
import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.exception.DuplicateAnswerException;
import cz.hackmeifyoucan.backend.repository.AnswerRepository;
import cz.hackmeifyoucan.backend.repository.PlayerRepository;
import cz.hackmeifyoucan.backend.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private AnswerServiceImpl answerService;

    @Test
    void given_correct_answer_when_submitting_then_should_add_all_score_components() {
        Player player = Player.builder().id(1L).nickname("tester").score(200).build();
        PhishingCategory first = PhishingCategory.builder().id(1L).tag("FAKE_URL").rewardPoints(600).build();
        PhishingCategory second = PhishingCategory.builder().id(2L).tag("URGENT").rewardPoints(500).build();
        Question question = Question.builder()
                .id(3L)
                .difficulty(Difficulty.EASY)
                .phishing(false)
                .categories(Set.of(first, second))
                .build();

        AnswerRequest request = new AnswerRequest(1L, 3L, "session-1", false, 10);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(questionRepository.findWithCategoriesById(3L)).thenReturn(Optional.of(question));
        when(answerRepository.existsById(new AnswerId(1L, 3L, "session-1"))).thenReturn(false);
        when(playerRepository.incrementScoreAtomically(1L, 200, 1300)).thenReturn(1);
        when(playerRepository.findScoreById(1L)).thenReturn(1500);

        AnswerResponse response = answerService.submitAnswer(request);

        assertThat(response.answerCorrect()).isTrue();
        assertThat(response.score()).isEqualTo(1500);
        verify(answerRepository).save(any());
        verify(playerRepository).incrementScoreAtomically(1L, 200, 1300);
    }

    @Test
    void given_wrong_answer_when_submitting_then_should_not_increase_player_score() {
        Player player = Player.builder().id(1L).nickname("tester").score(200).build();
        PhishingCategory category = PhishingCategory.builder().id(1L).tag("SPEAR_PHISH").rewardPoints(1000).build();
        Question question = Question.builder()
                .id(3L)
                .difficulty(Difficulty.HARD)
                .phishing(true)
                .categories(Set.of(category))
                .build();

        AnswerRequest request = new AnswerRequest(1L, 3L, "session-1", false, 20);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(questionRepository.findWithCategoriesById(3L)).thenReturn(Optional.of(question));
        when(answerRepository.existsById(new AnswerId(1L, 3L, "session-1"))).thenReturn(false);
        when(playerRepository.incrementScoreAtomically(1L, 200, 0)).thenReturn(1);
        when(playerRepository.findScoreById(1L)).thenReturn(200);

        AnswerResponse response = answerService.submitAnswer(request);

        assertThat(response.answerCorrect()).isFalse();
        assertThat(response.score()).isEqualTo(200);
        verify(answerRepository).save(any());
        verify(playerRepository).incrementScoreAtomically(1L, 200, 0);
    }

    @Test
    void given_existing_answer_when_submitting_then_should_throw_conflict_exception() {
        Player player = Player.builder().id(1L).nickname("tester").score(200).build();
        Question question = Question.builder()
                .id(3L)
                .difficulty(Difficulty.EASY)
                .phishing(true)
                .build();

        AnswerRequest request = new AnswerRequest(1L, 3L, "session-1", true, 5);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(questionRepository.findWithCategoriesById(3L)).thenReturn(Optional.of(question));
        when(answerRepository.existsById(new AnswerId(1L, 3L, "session-1"))).thenReturn(true);

        assertThatThrownBy(() -> answerService.submitAnswer(request))
                .isInstanceOf(DuplicateAnswerException.class);
        verify(answerRepository, never()).save(any());
        verify(playerRepository, never()).incrementScoreAtomically(any(), anyInt(), anyInt());
    }
}



