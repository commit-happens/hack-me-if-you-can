package cz.hackmeifyoucan.backend.service;

import cz.hackmeifyoucan.backend.dto.AnswerRequest;
import cz.hackmeifyoucan.backend.dto.AnswerResponse;

public interface AnswerService {

    AnswerResponse submitAnswer(AnswerRequest request);
}

