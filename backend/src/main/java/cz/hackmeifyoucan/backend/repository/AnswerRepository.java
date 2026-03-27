package cz.hackmeifyoucan.backend.repository;

import cz.hackmeifyoucan.backend.entity.Answer;
import cz.hackmeifyoucan.backend.entity.AnswerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, AnswerId> {


    @Query(value = """
            SELECT COALESCE(SUM(earned_points), 0)
            FROM answers
            WHERE player_id = :playerId
              AND session_id = :sessionId
            """, nativeQuery = true)
    int sumEarnedPointsByPlayerAndSession(@Param("playerId") Long playerId, @Param("sessionId") String sessionId);

    @Query(value = """
            SELECT COALESCE(SUM(CASE WHEN is_correct = false THEN difficulty_points + categories_points ELSE 0 END), 0)
            FROM answers
            WHERE player_id = :playerId
              AND session_id = :sessionId
            """, nativeQuery = true)
    int sumPotentialPointsForLatestWrongAnswersInSession(@Param("playerId") Long playerId, @Param("sessionId") String sessionId);
}


