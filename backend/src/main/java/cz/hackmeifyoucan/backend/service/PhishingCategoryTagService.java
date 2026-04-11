package cz.hackmeifyoucan.backend.service;

import cz.hackmeifyoucan.backend.entity.PhishingCategory;
import cz.hackmeifyoucan.backend.repository.PhishingCategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class PhishingCategoryTagService {

    private final PhishingCategoryRepository phishingCategoryRepository;
    private final AtomicReference<Map<String, ResolvedCategory>> tagCache = new AtomicReference<>(Map.of());

    public PhishingCategoryTagService(PhishingCategoryRepository phishingCategoryRepository) {
        this.phishingCategoryRepository = phishingCategoryRepository;
    }

    @PostConstruct
    public void warmUpCache() {
        refreshCache();
    }

    @Scheduled(fixedDelayString = "${app.category-tags-refresh-ms:300000}")
    public void refreshCache() {
        Map<String, ResolvedCategory> map = phishingCategoryRepository.findAll()
                .stream()
                .filter(category -> StringUtils.hasText(category.getTag()))
                .collect(Collectors.toMap(
                        category -> normalizeTag(category.getTag()),
                        category -> new ResolvedCategory(category.getId(), normalizeTag(category.getTag())),
                        (left, right) -> left,
                        TreeMap::new
                ));
        tagCache.set(Collections.unmodifiableMap(map));
    }

    public ResolvedCategory resolveTag(String rawTag) {
        String normalizedTag = normalizeTag(rawTag);
        ResolvedCategory resolvedCategory = tagCache.get().get(normalizedTag);
        if (resolvedCategory == null) {
            throw new IllegalArgumentException("Neplatná kategorie: " + rawTag + ". Povolené hodnoty: " + getAllowedTags());
        }
        return resolvedCategory;
    }

    public PhishingCategory resolveForSave(Long categoryId, String categoryTag) {
        if (StringUtils.hasText(categoryTag)) {
            ResolvedCategory resolvedByTag = resolveTag(categoryTag);
            if (categoryId != null && !categoryId.equals(resolvedByTag.id())) {
                throw new IllegalArgumentException("category_id neodpovídá category_tag: " + categoryId + " vs " + resolvedByTag.tag());
            }
            return findById(resolvedByTag.id());
        }

        if (categoryId != null) {
            return findById(categoryId);
        }

        throw new IllegalArgumentException("Musí být zadáno category_tag nebo category_id");
    }

    public Set<String> getAllowedTags() {
        return tagCache.get().values().stream().map(ResolvedCategory::tag).collect(Collectors.toCollection(java.util.TreeSet::new));
    }

    private PhishingCategory findById(Long categoryId) {
        return phishingCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Neplatná category_id: " + categoryId));
    }

    private String normalizeTag(String rawTag) {
        if (!StringUtils.hasText(rawTag)) {
            throw new IllegalArgumentException("category_tag je povinný");
        }
        return rawTag.trim().toUpperCase(Locale.ROOT);
    }

    public record ResolvedCategory(Long id, String tag) {
    }
}

