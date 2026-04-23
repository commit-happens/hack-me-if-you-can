package cz.hackmeifyoucan.backend.service;

import cz.hackmeifyoucan.backend.dto.PhishingCategoryResponse;

import java.util.List;

public interface PhishingCategoryService {

    List<PhishingCategoryResponse> getAllCategories();

    PhishingCategoryResponse getCategoryByTag(String tag);
}



