package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.dto.Error404Response;
import cz.hackmeifyoucan.backend.dto.Error500Response;
import cz.hackmeifyoucan.backend.dto.PhishingCategoryResponse;
import cz.hackmeifyoucan.backend.service.PhishingCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/phishing-categories")
@Tag(
        name = "phishing-category-controller",
        description = "Read-only endpointy pro phishing kategorie a jejich bezpečnostní doporučení"
)
public class PhishingCategoryController {

    private final PhishingCategoryService phishingCategoryService;

    public PhishingCategoryController(PhishingCategoryService phishingCategoryService) {
        this.phishingCategoryService = phishingCategoryService;
    }

    @Operation(
            summary = "Získat seznam všech phishing kategorií",
            description = "Vrátí kompletní seznam phishing kategorií včetně tagu, popisu, bodového ohodnocení a bezpečnostních doporučení."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Seznam kategorií byl úspěšně načten",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PhishingCategoryResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Neočekávaná chyba serveru",
                    content = @Content(schema = @Schema(implementation = Error500Response.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<PhishingCategoryResponse>> getAllPhishingCategories() {
        return ResponseEntity.ok(phishingCategoryService.getAllCategories());
    }

    @Operation(
            summary = "Získat detail phishing kategorie podle tagu",
            description = "Vrátí detail jedné phishing kategorie podle tagu. Vyhledávání tagu je case-insensitive."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Detail kategorie byl úspěšně načten",
                    content = @Content(schema = @Schema(implementation = PhishingCategoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kategorie s daným tagem nebyla nalezena",
                    content = @Content(schema = @Schema(implementation = Error404Response.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Neočekávaná chyba serveru",
                    content = @Content(schema = @Schema(implementation = Error500Response.class))
            )
    })
    @GetMapping("/{tag}")
    public ResponseEntity<PhishingCategoryResponse> getPhishingCategoryByTag(
            @Parameter(
                    description = "Tag phishing kategorie (např. LEGIT, FAKE_URL, URGENT). Hodnota je case-insensitive.",
                    example = "fake_url"
            )
            @PathVariable String tag
    ) {
        return ResponseEntity.ok(phishingCategoryService.getCategoryByTag(tag));
    }
}

