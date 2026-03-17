package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.QuestionResponse;
import cz.hackmeifyoucan.backend.repository.QuestionRepository;
import cz.hackmeifyoucan.backend.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getRandomQuestionsByDifficulty(int difficulty, int limit) {
        return questionRepository.getRandomQuestionsByDifficulty(difficulty, limit)
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
}
