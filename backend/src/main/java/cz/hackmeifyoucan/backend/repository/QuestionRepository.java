package cz.hackmeifyoucan.backend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cz.hackmeifyoucan.backend.entity.Question;
import cz.hackmeifyoucan.backend.enums.Difficulty;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(
            value = """
                    SELECT DISTINCT q
                    FROM Question q
                    JOIN FETCH q.platformType
                    WHERE q.difficulty = :difficulty
                    ORDER BY function('RANDOM')
                    """
    )
    List<Question> getRandomQuestionsByDifficulty(@Param("difficulty") Difficulty difficulty, Pageable pageable);
}
