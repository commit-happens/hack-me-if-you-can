package cz.hackmeifyoucan.backend.repository;

import java.util.List;

import cz.hackmeifyoucan.backend.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(
            value = """
                    SELECT q.*, pt.name AS platform_name
                    FROM questions AS q
                    JOIN platform_types AS pt ON q.platform_type_id = pt.id
                    WHERE q.difficulty = :difficulty
                    ORDER BY RANDOM()
                    LIMIT :limit
                    """,
            nativeQuery = true
    )
    List<Question> getRandomQuestionsByDifficulty(@Param("difficulty") int difficulty, Pageable pageable);
}
