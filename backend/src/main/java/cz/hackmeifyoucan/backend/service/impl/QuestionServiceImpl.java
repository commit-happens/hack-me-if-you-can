package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.EmailQuestionRequest;
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
import java.util.Set;

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
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public QuestionResponse saveEmailQuestion(EmailQuestionRequest request) {
        Map<String, String> metadata = Map.of(
                "sender", request.sender().trim(),
                "subject", request.subject().trim()
        );
        return saveQuestion(
                PlatformType.EMAIL,
                request.categoryId(),
                request.categoryTag(),
                request.difficulty(),
                request.phishing(),
                request.content().trim(),
                request.explanation().trim(),
                metadata
        );
    }

    @Override
    @Transactional
    public QuestionResponse saveSmsQuestion(SmsQuestionRequest request) {
        Map<String, String> metadata = Map.of(
                "sender", request.sender().trim(),
                "phoneNumber", request.phoneNumber().trim()
        );
        return saveQuestion(
                PlatformType.SMS,
                request.categoryId(),
                request.categoryTag(),
                request.difficulty(),
                request.phishing(),
                request.content().trim(),
                request.explanation().trim(),
                metadata
        );
    }

    private QuestionResponse saveQuestion(
            PlatformType platformType,
            Long categoryId,
            String categoryTag,
            Difficulty difficulty,
            boolean phishing,
            String content,
            String explanation,
            Map<String, String> metadata
    ) {
        PhishingCategory category = resolveCategory(categoryId, categoryTag);

        Question question = Question.builder()
                .platformType(platformType)
                .categories(Set.of(category))
                .phishing(phishing)
                .difficulty(difficulty)
                .penalty(0)
                .content(content)
                .metadata(metadata)
                .explanation(explanation)
                .build();

        Question saved = questionRepository.saveAndFlush(question);
        return toResponse(saved);
    }

    private QuestionResponse toResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getPlatformType().getName(),
                question.getMetadata(),
                question.getContent(),
                question.getExplanation()
        );
    }

    private PhishingCategory resolveCategory(Long categoryId, String categoryTag) {
        if (categoryId != null) {
            return phishingCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new PhishingCategoryNotFoundException(String.valueOf(categoryId)));
        }

        if (StringUtils.hasText(categoryTag)) {
            return phishingCategoryRepository.findByTagIgnoreCase(categoryTag.trim())
                    .orElseThrow(() -> new PhishingCategoryNotFoundException(categoryTag.trim()));
        }

        throw new IllegalArgumentException("category_id nebo category_tag je povinné");
    }

}