package cz.hackmeifyoucan.backend.service.impl;

import cz.hackmeifyoucan.backend.dto.PhishingCategoryLookupResponse;
import cz.hackmeifyoucan.backend.repository.PhishingCategoryRepository;
import cz.hackmeifyoucan.backend.service.PhishingCategoryService;
import org.springframework.data.domain.Sort;
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
    public List<PhishingCategoryLookupResponse> getAllCategories() {
        return phishingCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(category -> new PhishingCategoryLookupResponse(category.getId(), category.getTag()))
                .toList();
    }
}

