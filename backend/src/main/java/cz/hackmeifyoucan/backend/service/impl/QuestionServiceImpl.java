package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.ProblemResponse;
import cz.hackmeifyoucan.backend.dto.QuestionMetadataResponse;
import cz.hackmeifyoucan.backend.dto.QuestionRequest;
import cz.hackmeifyoucan.backend.dto.QuestionResponse;
import cz.hackmeifyoucan.backend.entity.Question;
import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.exception.PhishingCategoryNotFoundException;
import cz.hackmeifyoucan.backend.repository.PhishingCategoryRepository;
import cz.hackmeifyoucan.backend.repository.QuestionRepository;
import cz.hackmeifyoucan.backend.service.QuestionService;
import cz.hackmeifyoucan.backend.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final PhishingCategoryRepository phishingCategoryRepository;
    private final ProblemService problemService;

    private static final Pattern TAG_PATTERN = Pattern.compile("\\{\\{[^|]*\\|([^}]+)");

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
    public QuestionResponse saveQuestion(QuestionRequest req) {
        return saveQuestionInternal(req);
    }

    @Override
    @Transactional
    public List<QuestionResponse> saveQuestions(List<QuestionRequest> requests) {
        return requests.stream()
                .map(this::saveQuestionInternal)
                .toList();
    }

    private QuestionResponse saveQuestionInternal(QuestionRequest req) {
        Question q = questionRepository.saveAndFlush(Question.builder()
                .platformType(req.getPlatformType())
                .phishingCategory(phishingCategoryRepository.findByTagIgnoreCase(req.categoryTag())
                        .orElseThrow(() -> new PhishingCategoryNotFoundException(req.categoryTag())))
                .phishing(req.phishing())
                .difficulty(Difficulty.valueOf(req.difficulty().toUpperCase()))
                .content(req.content().trim())
                .metadata(Map.of("sender", req.sender().trim(), "subject", req.subject().trim()))
                .explanation(req.explanation().trim())
                .problems(new ArrayList<>())
                .build());

        extractTags(req.content()).forEach(tag -> problemService.assignProblemToQuestion(q.getId(), tag));

        return toResponse(questionRepository.findById(q.getId()).orElseThrow());
    }

    private Set<String> extractTags(String content) {
        Set<String> tags = new java.util.HashSet<>();
        Matcher m = TAG_PATTERN.matcher(content);
        while (m.find()) tags.add(m.group(1).trim());
        return tags;
    }

    private QuestionResponse toResponse(Question q) {
        return new QuestionResponse(
                q.getId(),
                q.getPlatformType().getName(),
                new QuestionMetadataResponse(q.getMetadata().get("sender"), q.getMetadata().get("subject")),
                q.getContent(),
                q.getExplanation(),
                q.getProblems().stream().map(p -> new ProblemResponse(p.getTag(), p.getDescription())).toList()
        );
    }

}