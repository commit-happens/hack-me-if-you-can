package cz.hackmeifyoucan.backend.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cz.hackmeifyoucan.backend.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    boolean existsByNickname(String nickname);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            UPDATE players
            SET score = GREATEST(score, :initialScore) + :earnedPoints
            WHERE id = :playerId
            """, nativeQuery = true)
    int incrementScoreAtomically(
            @Param("playerId") Long playerId,
            @Param("initialScore") int initialScore,
            @Param("earnedPoints") int earnedPoints
    );

    @Query(value = """
            SELECT score
            FROM players
            WHERE id = :playerId
            """, nativeQuery = true)
    Integer findScoreById(@Param("playerId") Long playerId);
}