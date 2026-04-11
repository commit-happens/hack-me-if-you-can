package cz.hackmeifyoucan.backend.service;

import cz.hackmeifyoucan.backend.dto.AdminQuestionResponse;
import cz.hackmeifyoucan.backend.dto.EmailQuestionCreateRequest;
import cz.hackmeifyoucan.backend.dto.QuestionResponse;
import cz.hackmeifyoucan.backend.dto.SmsQuestionCreateRequest;
import cz.hackmeifyoucan.backend.enums.Difficulty;

import java.util.List;

public interface QuestionService {

    List<QuestionResponse> getRandomQuestionsByDifficulty(Difficulty difficulty, int limit);

    AdminQuestionResponse saveEmailQuestion(EmailQuestionCreateRequest request);

    AdminQuestionResponse saveSmsQuestion(SmsQuestionCreateRequest request);
}
