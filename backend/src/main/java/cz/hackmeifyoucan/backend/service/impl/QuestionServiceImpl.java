package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.AdminQuestionResponse;
import cz.hackmeifyoucan.backend.dto.EmailQuestionCreateRequest;
import cz.hackmeifyoucan.backend.dto.QuestionResponse;
import cz.hackmeifyoucan.backend.dto.SmsQuestionCreateRequest;
import cz.hackmeifyoucan.backend.entity.PhishingCategory;
import cz.hackmeifyoucan.backend.entity.Question;
import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.enums.PlatformType;
import cz.hackmeifyoucan.backend.repository.QuestionRepository;
import cz.hackmeifyoucan.backend.service.PhishingCategoryTagService;
import cz.hackmeifyoucan.backend.service.QuestionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final int LEGACY_DEFAULT_PENALTY = 0;

    private final QuestionRepository questionRepository;
    private final PhishingCategoryTagService categoryTagService;

    public QuestionServiceImpl(
            QuestionRepository questionRepository,
            PhishingCategoryTagService categoryTagService
    ) {
        this.questionRepository = questionRepository;
        this.categoryTagService = categoryTagService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getRandomQuestionsByDifficulty(Difficulty difficulty, int limit) {
        return questionRepository.getRandomQuestionsByDifficulty(difficulty.getLevel(), PageRequest.of(0, limit))
                .stream()
                .map(question -> new QuestionResponse(
                        question.getId(),
                        question.getPlatformType().getName(),
                        question.getMetadata(),
                        question.getContent(),
                        question.getExplanation()
                ))
                .toList();
    }

    @Override
    @Transactional
    public AdminQuestionResponse saveEmailQuestion(EmailQuestionCreateRequest request) {
        Map<String, String> metadata = Map.of(
                "sender", request.sender().trim(),
                "subject", request.subject().trim()
        );
        return saveQuestion(
                PlatformType.EMAIL,
                request.categoryId(),
                request.categoryTag(),
                normalizeDifficulty(request.difficulty()),
                request.phishing(),
                request.content().trim(),
                request.explanation().trim(),
                metadata
        );
    }

    @Override
    @Transactional
    public AdminQuestionResponse saveSmsQuestion(SmsQuestionCreateRequest request) {
        Map<String, String> metadata = Map.of(
                "sender", request.sender().trim(),
                "phoneNumber", request.phoneNumber().trim()
        );
        return saveQuestion(
                PlatformType.SMS,
                request.categoryId(),
                request.categoryTag(),
                normalizeDifficulty(request.difficulty()),
                request.phishing(),
                request.content().trim(),
                request.explanation().trim(),
                metadata
        );
    }

    private AdminQuestionResponse saveQuestion(
            PlatformType platformType,
            Long categoryId,
            String categoryTag,
            Difficulty difficulty,
            boolean phishing,
            String content,
            String explanation,
            Map<String, String> metadata
    ) {
        PhishingCategory category = categoryTagService.resolveForSave(categoryId, categoryTag);

        Question question = Question.builder()
                .platformType(platformType)
                .category(category)
                .phishing(phishing)
                .difficulty(difficulty)
                .penalty(LEGACY_DEFAULT_PENALTY)
                .content(content)
                .metadata(metadata)
                .explanation(explanation)
                .build();

        Question saved = questionRepository.saveAndFlush(question);
        Question persisted = questionRepository.findById(saved.getId())
                .orElseThrow(() -> new IllegalStateException("Uložená otázka nebyla nalezena"));

        return toAdminResponse(persisted);
    }

    private AdminQuestionResponse toAdminResponse(Question question) {
        return new AdminQuestionResponse(
                question.getId(),
                question.getPlatformType().getName(),
                question.getDifficulty().name(),
                question.getCategory().getId(),
                question.isPhishing(),
                question.getMetadata(),
                question.getContent(),
                question.getExplanation(),
                question.getPenalty(),
                question.getCreatedAt()
        );
    }

    private Difficulty normalizeDifficulty(String difficultyRaw) {
        if (!StringUtils.hasText(difficultyRaw)) {
            throw new IllegalArgumentException("difficulty je povinné");
        }
        try {
            return Difficulty.valueOf(difficultyRaw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Neplatná difficulty: " + difficultyRaw);
        }
    }

}
