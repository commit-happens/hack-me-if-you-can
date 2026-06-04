package cz.hackmeifyoucan.backend.repository;

import cz.hackmeifyoucan.backend.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findByTag(String tag);
}


