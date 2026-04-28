package cz.hackmeifyoucan.backend.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cz.hackmeifyoucan.backend.common.ScoringConstants;
import cz.hackmeifyoucan.backend.dto.PlayerRequest;
import cz.hackmeifyoucan.backend.dto.PlayerResponse;
import cz.hackmeifyoucan.backend.dto.PlayerSummaryResponse;
import cz.hackmeifyoucan.backend.entity.Player;
import cz.hackmeifyoucan.backend.exception.DuplicateNicknameException;
import cz.hackmeifyoucan.backend.exception.PlayerNotFoundException;
import cz.hackmeifyoucan.backend.repository.AnswerRepository;
import cz.hackmeifyoucan.backend.repository.PlayerRepository;
import cz.hackmeifyoucan.backend.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final AnswerRepository answerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, AnswerRepository answerRepository) {
        this.playerRepository = playerRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    @Transactional
    public PlayerResponse addPlayer(PlayerRequest playerRequest) {
        if (playerRepository.existsByNickname(playerRequest.nickname())) {
            throw new DuplicateNicknameException(playerRequest.nickname());
        }
        Player player = new Player();
        player.setNickname(playerRequest.nickname());
        Player savedPlayer = playerRepository.save(player);
        return convertToResponse(savedPlayer, ScoringConstants.INITIAL_SCORE);
    }

    @Override
    public PlayerResponse getPlayerById(Long playerId) {
        Player player = findPlayerOrThrow(playerId);
        int score = ScoringConstants.INITIAL_SCORE + answerRepository.sumEarnedPointsByPlayer(playerId);
        return convertToResponse(player, score);
    }

    @Override
    public List<PlayerResponse> getPlayers() {
        List<PlayerResponse> responseList = new ArrayList<>();
        for (Player player : playerRepository.findAll()) {
            int score = ScoringConstants.INITIAL_SCORE + answerRepository.sumEarnedPointsByPlayer(player.getId());
            responseList.add(convertToResponse(player, score));
        }
        return responseList;
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerSummaryResponse getPlayerSummary(Long playerId, String sessionId) {
        findPlayerOrThrow(playerId);
        if (!StringUtils.hasText(sessionId)) {
            int currentScore = ScoringConstants.INITIAL_SCORE + answerRepository.sumEarnedPointsByPlayer(playerId);
            return new PlayerSummaryResponse(playerId, null, currentScore, currentScore);
        }
        int score = ScoringConstants.INITIAL_SCORE + answerRepository.sumEarnedPointsByPlayerAndSession(playerId, sessionId);
        int potentialMissingPoints = answerRepository.sumPotentialPointsForWrongAnswersInSession(playerId, sessionId);
        return new PlayerSummaryResponse(playerId, sessionId, score, score + potentialMissingPoints);
    }

    private Player findPlayerOrThrow(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    private PlayerResponse convertToResponse(Player player, int score) {
        return new PlayerResponse(player.getId(), player.getNickname(), score);
    }

}
