package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.PhishingCategoryResponse;
import cz.hackmeifyoucan.backend.entity.PhishingCategory;
import cz.hackmeifyoucan.backend.exception.PhishingCategoryNotFoundException;
import cz.hackmeifyoucan.backend.repository.PhishingCategoryRepository;
import cz.hackmeifyoucan.backend.service.PhishingCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhishingCategoryServiceImpl implements PhishingCategoryService {

    private final PhishingCategoryRepository phishingCategoryRepository;

    public PhishingCategoryServiceImpl(PhishingCategoryRepository phishingCategoryRepository) {
        this.phishingCategoryRepository = phishingCategoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhishingCategoryResponse> getAllCategories() {
        return phishingCategoryRepository.findAllByOrderByTagAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PhishingCategoryResponse getCategoryByTag(String tag) {
        return phishingCategoryRepository.findByTagIgnoreCase(tag)
                .map(this::toResponse)
                .orElseThrow(() -> new PhishingCategoryNotFoundException(tag));
    }

    private PhishingCategoryResponse toResponse(PhishingCategory category) {
        return new PhishingCategoryResponse(
                category.getId(),
                category.getTag(),
                category.getDescription()
        );
    }
}




