package cz.hackmeifyoucan.backend.service;

import cz.hackmeifyoucan.backend.dto.PhishingCategoryLookupResponse;

import java.util.List;

public interface PhishingCategoryService {

    List<PhishingCategoryLookupResponse> getAllCategories();
}

