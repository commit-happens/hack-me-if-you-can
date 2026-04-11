package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lookup phishing kategorie")
public record PhishingCategoryLookupResponse(

        @Schema(description = "ID kategorie", example = "3")
        Long id,

        @Schema(description = "Canonical tag kategorie", example = "URGENT")
        String tag
) {
}

