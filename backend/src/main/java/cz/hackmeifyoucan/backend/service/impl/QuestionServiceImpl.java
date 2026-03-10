package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.QuestionResponse;
import cz.hackmeifyoucan.backend.entity.PhishingCategory;
import cz.hackmeifyoucan.backend.repository.QuestionRepository;
import cz.hackmeifyoucan.backend.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getRandomQuestionByDifficulty(int difficulty, int limit) {
        return questionRepository.getRandomQuestionsByDifficulty(difficulty, limit)
                .stream()
                .map(question -> {
                    List<Long> categoryIds = question.getCategories().stream()
                            .map(PhishingCategory::getId)
                            .sorted(Comparator.naturalOrder())
                            .collect(Collectors.toList());

                    return new QuestionResponse(
                            question.getId(),
                            question.getPlatformType().getName(),
                            question.getMetadata(),
                            question.getContent(),
                            question.getExplanation(),
                            question.isPhishing(),
                            categoryIds
                    );
                })
                .collect(Collectors.toList());
    }
}
