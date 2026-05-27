package cz.hackmeifyoucan.backend.service;

import cz.hackmeifyoucan.backend.entity.Problem;
import cz.hackmeifyoucan.backend.entity.QuestionProblem;
import cz.hackmeifyoucan.backend.repository.ProblemRepository;
import cz.hackmeifyoucan.backend.repository.QuestionProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final QuestionProblemRepository questionProblemRepository;

    public void assignProblemToQuestion(Long questionId, String problemTag) {
        Problem problem = problemRepository.findByTag(problemTag)
                .orElseThrow(() -> new IllegalArgumentException("Problem s tagem '" + problemTag + "' nebyl nalezen"));

        questionProblemRepository.save(QuestionProblem.builder()
                .questionId(questionId)
                .problemId(problem.getId())
                .build());
    }
}
