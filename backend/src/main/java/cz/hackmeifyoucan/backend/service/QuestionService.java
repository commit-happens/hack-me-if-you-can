package cz.hackmeifyoucan.backend.service;

import cz.hackmeifyoucan.backend.dto.QuestionRequest;
import cz.hackmeifyoucan.backend.dto.QuestionResponse;
import cz.hackmeifyoucan.backend.enums.Difficulty;

import java.util.List;

public interface QuestionService {

    List<QuestionResponse> getRandomQuestionsByDifficulty(Difficulty difficulty, int limit);

    QuestionResponse saveQuestion(QuestionRequest request);

    List<QuestionResponse> saveQuestions(List<QuestionRequest> requests);
}