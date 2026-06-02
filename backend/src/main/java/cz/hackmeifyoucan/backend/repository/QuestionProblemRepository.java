package cz.hackmeifyoucan.backend.repository;

import cz.hackmeifyoucan.backend.entity.QuestionProblem;
import cz.hackmeifyoucan.backend.entity.QuestionProblemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionProblemRepository extends JpaRepository<QuestionProblem, QuestionProblemId> {

    List<QuestionProblem> findByQuestionId(Long questionId);

    void deleteByQuestionId(Long questionId);
}

