package cz.hackmeifyoucan.backend.repository;

import cz.hackmeifyoucan.backend.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q JOIN FETCH q.platformType WHERE q.difficulty = :difficulty ORDER BY function('RANDOM')")
    List<Question> getRandomQuestionsByDifficulty(@Param("difficulty") int difficulty, Pageable pageable);
}
