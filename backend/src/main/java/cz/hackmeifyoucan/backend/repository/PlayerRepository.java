package cz.hackmeifyoucan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.hackmeifyoucan.backend.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    boolean existsByNickname(String nickname);
}