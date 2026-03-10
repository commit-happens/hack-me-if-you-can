package cz.hackmeifyoucan.backend.service;

import cz.hackmeifyoucan.backend.dto.QuestionResponse;

import java.util.List;

public interface QuestionService {

    List<QuestionResponse> getRandomQuestionByDifficulty(int difficulty, int limit);

}
