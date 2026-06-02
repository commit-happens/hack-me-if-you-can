package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.ProblemResponse;
import cz.hackmeifyoucan.backend.dto.QuestionMetadataResponse;
import cz.hackmeifyoucan.backend.dto.QuestionResponse;
import cz.hackmeifyoucan.backend.dto.SmsQuestionRequest;
import cz.hackmeifyoucan.backend.entity.PhishingCategory;
import cz.hackmeifyoucan.backend.entity.Question;
import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.enums.PlatformType;
import cz.hackmeifyoucan.backend.exception.PhishingCategoryNotFoundException;
import cz.hackmeifyoucan.backend.repository.PhishingCategoryRepository;
import cz.hackmeifyoucan.backend.repository.QuestionRepository;
import cz.hackmeifyoucan.backend.service.QuestionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final PhishingCategoryRepository phishingCategoryRepository;

    public QuestionServiceImpl(
            QuestionRepository questionRepository,
            PhishingCategoryRepository phishingCategoryRepository
    ) {
        this.questionRepository = questionRepository;
        this.phishingCategoryRepository = phishingCategoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getRandomQuestionsByDifficulty(Difficulty difficulty, int limit) {
        return questionRepository.getRandomQuestionsByDifficulty(difficulty.getLevel(), PageRequest.of(0, limit))
                .stream()
                .map(question -> new QuestionResponse(
                        question.getId(),
                        question.getPlatformType().getName(),
                toMetadataResponse(question.getMetadata()),
                        question.getContent(),
                        question.getExplanation(),
                        question.getProblems().stream()
                                .map(p -> new ProblemResponse(p.getTag(), p.getDescription()))
                                .toList()
                ))
                .toList();
    }

        private QuestionMetadataResponse toMetadataResponse(Map<String, String> metadata) {
        return new QuestionMetadataResponse(
            metadata.getOrDefault("sender", ""),
            metadata.getOrDefault("subject", "")
        );
        }
}
