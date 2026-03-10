package cz.hackmeifyoucan.backend.repository;

import cz.hackmeifyoucan.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(
        value = "SELECT * FROM questions WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT :limit",
        nativeQuery = true
    )
    List<Question> getRandomQuestionsByDifficulty(@Param("difficulty") int difficulty, @Param("limit") int limit);
}
