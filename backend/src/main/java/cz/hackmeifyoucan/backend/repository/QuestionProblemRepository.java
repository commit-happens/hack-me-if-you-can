package cz.hackmeifyoucan.backend.repository;

import cz.hackmeifyoucan.backend.entity.QuestionProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionProblemRepository extends JpaRepository<QuestionProblem, Long> {

    List<QuestionProblem> findByQuestionId(Long questionId);

    void deleteByQuestionId(Long questionId);
}

