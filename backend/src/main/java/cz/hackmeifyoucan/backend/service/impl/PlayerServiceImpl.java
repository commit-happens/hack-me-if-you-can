package cz.hackmeifyoucan.backend.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cz.hackmeifyoucan.backend.dto.PlayerRequest;
import cz.hackmeifyoucan.backend.dto.PlayerUpdateRequest;
import cz.hackmeifyoucan.backend.dto.PlayerResponse;
import cz.hackmeifyoucan.backend.dto.PlayerSummaryResponse;
import cz.hackmeifyoucan.backend.entity.Player;
import cz.hackmeifyoucan.backend.exception.PlayerNotFoundException;
import cz.hackmeifyoucan.backend.exception.DuplicateNicknameException;
import cz.hackmeifyoucan.backend.repository.AnswerRepository;
import cz.hackmeifyoucan.backend.repository.PlayerRepository;
import cz.hackmeifyoucan.backend.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final int INITIAL_SCORE = 200;

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
        player.setScore(INITIAL_SCORE);
        Player savedPlayer = playerRepository.save(player);
        return convertToResponse(savedPlayer);
    }

    @Override
    public PlayerResponse getPlayerById(Long playerId) {
        Player player = findPlayerOrThrow(playerId);
        return convertToResponse(player);
    }

    @Override
    public List<PlayerResponse> getPlayers() {
        List<PlayerResponse> responseList = new ArrayList<>();
        Iterable<Player> allPlayers = playerRepository.findAll();
        for (Player player : allPlayers) {
            PlayerResponse response = convertToResponse(player);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    @Transactional
    public PlayerResponse updatePlayer(Long playerId, PlayerUpdateRequest request) {
        Player player = findPlayerOrThrow(playerId);
        if (request.nickname() != null && !request.nickname().isBlank()) {
            if (!request.nickname().equals(player.getNickname()) && playerRepository.existsByNickname(request.nickname())) {
                throw new DuplicateNicknameException(request.nickname());
            }
            player.setNickname(request.nickname());
        }
        if (request.score() != null) {
            player.setScore(request.score());
        }
        Player updatedPlayer = playerRepository.save(player);
        return convertToResponse(updatedPlayer);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerSummaryResponse getPlayerSummary(Long playerId, String sessionId) {
        findPlayerOrThrow(playerId);
        if (!StringUtils.hasText(sessionId)) {
            return new PlayerSummaryResponse(playerId, null, INITIAL_SCORE, INITIAL_SCORE);
        }
        int score = INITIAL_SCORE + answerRepository.sumEarnedPointsByPlayerAndSession(playerId, sessionId);
        int potentialMissingPoints = answerRepository.sumPotentialPointsForLatestWrongAnswersInSession(playerId, sessionId);
        return new PlayerSummaryResponse(playerId, sessionId, score, score + potentialMissingPoints);
    }

    private Player findPlayerOrThrow(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    private PlayerResponse convertToResponse(Player player) {
        return new PlayerResponse(
            player.getId(),
            player.getNickname(),
            player.getScore()
        );
    }

}