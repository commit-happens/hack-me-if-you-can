package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.dto.Error400Response;
import cz.hackmeifyoucan.backend.dto.Error401Response;
import cz.hackmeifyoucan.backend.dto.Error429Response;
import cz.hackmeifyoucan.backend.dto.Error500Response;
import cz.hackmeifyoucan.backend.dto.llm.LlmGenerateEmailQuestionResponse;
import cz.hackmeifyoucan.backend.dto.llm.LlmGenerateQuestionRequest;
import cz.hackmeifyoucan.backend.dto.llm.LlmGenerateQuestionResponse;
import cz.hackmeifyoucan.backend.dto.llm.LlmGenerateSmsQuestionResponse;
import cz.hackmeifyoucan.backend.llm.GeminiLlmClient;
import cz.hackmeifyoucan.backend.service.PhishingCategoryTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

@RestController
@RequestMapping("/api/admin/llm")
@RequiredArgsConstructor
@Tag(name = "admin-controller", description = "Endpointy pro generování otázek pomocí LLM")
public class AdminLlmGeneratorController {

    private final GeminiLlmClient llmClient;
    private final PhishingCategoryTagService categoryTagService;

    @GetMapping("/generate-question")
    @Operation(
            summary = "Vygenerování otázky přes LLM",
            description = "Vygeneruje návrh otázky pro EMAIL nebo SMS. Kategorie je validována case-insensitive proti phishing_categories a do response se vrací canonical tag.",
            parameters = {
                    @Parameter(name = "X-Internal-Api-Key", in = ParameterIn.HEADER, required = true,
                            description = "Interní API klíč pro přístup na admin endpointy")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Otázka byla úspěšně vygenerována",
                    content = @Content(schema = @Schema(oneOf = {
                            LlmGenerateEmailQuestionResponse.class,
                            LlmGenerateSmsQuestionResponse.class
                    }))),
            @ApiResponse(responseCode = "400", description = "Neplatný request (parametry nebo neexistující category)",
                    content = @Content(schema = @Schema(implementation = Error400Response.class))),
            @ApiResponse(responseCode = "401", description = "Neautorizovaný interní přístup",
                    content = @Content(schema = @Schema(implementation = Error401Response.class))),
            @ApiResponse(responseCode = "429", description = "Byl překročen limit volání AI služby",
                    content = @Content(schema = @Schema(implementation = Error429Response.class))),
            @ApiResponse(responseCode = "500", description = "Neočekávaná chyba serveru",
                    content = @Content(schema = @Schema(implementation = Error500Response.class)))
    })
    public LlmGenerateQuestionResponse generateDraft(@ParameterObject LlmGenerateQuestionRequest request) {
        PhishingCategoryTagService.ResolvedCategory resolvedCategory = categoryTagService.resolveTag(request.category());

        return llmClient.generate(
            request.platform(),
            resolvedCategory.tag(),
            request.difficulty().name(),
            request.getLanguageOrDefault()
        );
    }
}