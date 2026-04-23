package cz.hackmeifyoucan.backend.repository;

import cz.hackmeifyoucan.backend.entity.PhishingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhishingCategoryRepository extends JpaRepository<PhishingCategory, Long> {

    List<PhishingCategory> findAllByOrderByTagAsc();

    Optional<PhishingCategory> findByTagIgnoreCase(String tag);
}

