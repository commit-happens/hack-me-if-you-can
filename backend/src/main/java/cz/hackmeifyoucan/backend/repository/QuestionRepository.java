package cz.hackmeifyoucan.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cz.hackmeifyoucan.backend.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(
            value = """
                    SELECT *
                    FROM questions q
                    WHERE q.difficulty = :difficultyLevel
                    ORDER BY RANDOM()
                    """,
            nativeQuery = true
    )
    List<Question> getRandomQuestionsByDifficulty(@Param("difficultyLevel") int difficultyLevel, Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    Optional<Question> findWithCategoryById(Long id);
}
