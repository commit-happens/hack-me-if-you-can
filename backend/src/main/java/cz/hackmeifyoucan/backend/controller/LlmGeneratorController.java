package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.dto.Error400Response;
import cz.hackmeifyoucan.backend.dto.Error404Response;
import cz.hackmeifyoucan.backend.dto.Error429Response;
import cz.hackmeifyoucan.backend.dto.Error500Response;
import cz.hackmeifyoucan.backend.dto.LlmGenerateEmailQuestionResponse;
import cz.hackmeifyoucan.backend.dto.LlmGenerateQuestionRequest;
import cz.hackmeifyoucan.backend.dto.LlmGenerateSmsQuestionResponse;
import cz.hackmeifyoucan.backend.dto.PhishingCategoryResponse;
import cz.hackmeifyoucan.backend.llm.GeminiLlmClient;
import cz.hackmeifyoucan.backend.service.PhishingCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/llm")
@RequiredArgsConstructor
@Tag(name = "llm-controller", description = "Endpointy pro práci s LLM (např. generování otázek)")
public class LlmGeneratorController {

    private final GeminiLlmClient llmClient;
    private final PhishingCategoryService categoryTagService;

    @GetMapping("/generate-question")
    @Operation(
            summary = "Vygenerování otázky přes Gemini LLM",
            description = "Vygeneruje návrh otázky pro vybranou kategori, platformu a jazyk - vrácený jako json."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Otázka byla úspěšně vygenerována",
                    content = @Content(schema = @Schema(oneOf = {
                            LlmGenerateEmailQuestionResponse.class,
                            LlmGenerateSmsQuestionResponse.class
                    }))),
            @ApiResponse(responseCode = "400", description = "Neplatný request (parametry nebo neexistující category)",
                    content = @Content(schema = @Schema(implementation = Error400Response.class))),
            @ApiResponse(responseCode = "404", description = "Kategorie nebyla nalezena",
                    content = @Content(schema = @Schema(implementation = Error404Response.class))),
            @ApiResponse(responseCode = "429", description = "Byl překročen limit volání AI služby",
                    content = @Content(schema = @Schema(implementation = Error429Response.class))),
            @ApiResponse(responseCode = "500", description = "Neočekávaná chyba serveru",
                    content = @Content(schema = @Schema(implementation = Error500Response.class)))
    })
    public Object generateDraft(@Valid @ParameterObject LlmGenerateQuestionRequest request) {
        PhishingCategoryResponse resolvedCategory = categoryTagService.getCategoryByTag(request.category());

        return llmClient.generate(
                request.platform(),
                resolvedCategory.tag(),
                request.difficulty().name(),
                request.getLanguageOrDefault()
        );
    }
}