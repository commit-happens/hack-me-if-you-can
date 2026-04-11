package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.dto.Error400Response;
import cz.hackmeifyoucan.backend.dto.Error401Response;
import cz.hackmeifyoucan.backend.dto.Error500Response;
import cz.hackmeifyoucan.backend.dto.PhishingCategoryLookupResponse;
import cz.hackmeifyoucan.backend.service.PhishingCategoryTagService;
import cz.hackmeifyoucan.backend.service.PhishingCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "admin-controller", description = "Endpointy pro správu a validaci phishingových kategorií")
public class AdminPhishingCategoryController {

    private final PhishingCategoryService phishingCategoryService;
    private final PhishingCategoryTagService categoryTagService;

    @GetMapping
    @Operation(
        summary = "Seznam všech phishingových kategorií",
        description = "Vrací seznam všech validních phishing kategorií (id + canonical tag) pro interní nástroje.",
        parameters = {
            @Parameter(name = "X-Internal-Api-Key", in = ParameterIn.HEADER, required = true,
                description = "Interní API klíč pro přístup na admin endpointy")
        }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seznam kategorií",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PhishingCategoryLookupResponse.class)))),
        @ApiResponse(responseCode = "401", description = "Neautorizovaný interní přístup",
            content = @Content(schema = @Schema(implementation = Error401Response.class))),
        @ApiResponse(responseCode = "500", description = "Neočekávaná chyba serveru",
            content = @Content(schema = @Schema(implementation = Error500Response.class)))
    })
    public List<PhishingCategoryLookupResponse> getAllCategories() {
        return phishingCategoryService.getAllCategories();
    }

    @GetMapping("/resolve")
    @Operation(
        summary = "Najde kategorii podle tagu",
        description = "Provádí case-insensitive validaci tagu proti DB cache a vrací canonical tag s id.",
        parameters = {
            @Parameter(name = "X-Internal-Api-Key", in = ParameterIn.HEADER, required = true,
                description = "Interní API klíč pro přístup na admin endpointy"),
            @Parameter(name = "tag", required = true,
                description = "Tag phishing kategorie (case-insensitive). Např. legit, URGENT.")
        }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kategorie byla nalezena",
            content = @Content(schema = @Schema(implementation = PhishingCategoryLookupResponse.class))),
        @ApiResponse(responseCode = "400", description = "Neplatný nebo neexistující tag",
            content = @Content(schema = @Schema(implementation = Error400Response.class))),
        @ApiResponse(responseCode = "401", description = "Neautorizovaný interní přístup",
            content = @Content(schema = @Schema(implementation = Error401Response.class))),
        @ApiResponse(responseCode = "500", description = "Neočekávaná chyba serveru",
            content = @Content(schema = @Schema(implementation = Error500Response.class)))
    })
    public PhishingCategoryLookupResponse resolveCategory(@RequestParam("tag") String tag) {
        PhishingCategoryTagService.ResolvedCategory resolvedCategory = categoryTagService.resolveTag(tag);
        return new PhishingCategoryLookupResponse(
            resolvedCategory.id(),
            resolvedCategory.tag()
        );
    }
}