package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.common.ScoringConstants;
import cz.hackmeifyoucan.backend.dto.AnswerRequest;
import cz.hackmeifyoucan.backend.dto.AnswerResponse;
import cz.hackmeifyoucan.backend.entity.*;
import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.exception.DuplicateAnswerException;
import cz.hackmeifyoucan.backend.exception.PlayerNotFoundException;
import cz.hackmeifyoucan.backend.exception.QuestionNotFoundException;
import cz.hackmeifyoucan.backend.repository.AnswerRepository;
import cz.hackmeifyoucan.backend.repository.PlayerRepository;
import cz.hackmeifyoucan.backend.repository.QuestionRepository;
import cz.hackmeifyoucan.backend.service.AnswerService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerServiceImpl implements AnswerService {

    private static final int SPEED_MULTIPLIER = 10;

    private final AnswerRepository answerRepository;
    private final PlayerRepository playerRepository;
    private final QuestionRepository questionRepository;

    public AnswerServiceImpl(
            AnswerRepository answerRepository,
            PlayerRepository playerRepository,
            QuestionRepository questionRepository
    ) {
        this.answerRepository = answerRepository;
        this.playerRepository = playerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional
    public AnswerResponse submitAnswer(AnswerRequest request) {
        Player player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new PlayerNotFoundException(request.playerId()));

        Question question = questionRepository.findWithPhishingCategoryById(request.questionId())
                .orElseThrow(() -> new QuestionNotFoundException(request.questionId()));

        AnswerId answerId = new AnswerId(request.playerId(), request.questionId(), request.sessionId());

        int difficultyPoints = toDifficultyPoints(question.getDifficulty());
        int categoriesPoints = question.getPhishingCategory().getRewardPoints();
        int speedBonus = request.remainTime() * SPEED_MULTIPLIER;

        boolean answerCorrect = request.phishing().equals(question.isPhishing());
        int earnedPoints = answerCorrect ? difficultyPoints + categoriesPoints + speedBonus : 0;

        Answer answer = Answer.builder()
                .id(answerId)
                .player(player)
                .question(question)
                .correct(answerCorrect)
                .earnedPoints(earnedPoints)
                .difficultyPoints(difficultyPoints)
                .categoriesPoints(categoriesPoints)
                .speedBonus(answerCorrect ? speedBonus : 0)
                .build();
        try {
            answerRepository.saveAndFlush(answer);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateAnswerException(request.playerId(), request.questionId(), request.sessionId());
        }

        int updatedScore = ScoringConstants.INITIAL_SCORE + answerRepository.sumEarnedPointsByPlayer(player.getId());

        return new AnswerResponse(answerCorrect, updatedScore);
    }

    private int toDifficultyPoints(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 100;
            case MEDIUM -> 250;
            case HARD -> 500;
        };
    }
}
