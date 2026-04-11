package cz.hackmeifyoucan.backend.repository;

import cz.hackmeifyoucan.backend.entity.PhishingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhishingCategoryRepository extends JpaRepository<PhishingCategory, Long> {
}
