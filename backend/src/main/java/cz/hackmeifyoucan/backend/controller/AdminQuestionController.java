package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.dto.AdminQuestionResponse;
import cz.hackmeifyoucan.backend.dto.EmailQuestionCreateRequest;
import cz.hackmeifyoucan.backend.dto.Error400Response;
import cz.hackmeifyoucan.backend.dto.Error401Response;
import cz.hackmeifyoucan.backend.dto.Error409Response;
import cz.hackmeifyoucan.backend.dto.Error500Response;
import cz.hackmeifyoucan.backend.dto.SmsQuestionCreateRequest;
import cz.hackmeifyoucan.backend.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/questions")
@Tag(name = "admin-controller", description = "Endpointy pro ukládání otázek do databáze Questions")
public class AdminQuestionController {

    private final QuestionService questionService;

    public AdminQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/email")
    @Operation(
            summary = "Uložení emailové otázky",
            description = "Uloží novou emailovou otázku do databáze. Kategorie se mapuje přes category_tag nebo legacy category_id.",
            parameters = {
                    @Parameter(name = "X-Internal-Api-Key", in = ParameterIn.HEADER, required = true,
                            description = "Interní API klíč pro přístup na admin endpointy")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Otázka byla úspěšně uložena",
                    content = @Content(schema = @Schema(implementation = AdminQuestionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Neplatný request (validace, neplatná difficulty nebo kategorie)",
                    content = @Content(schema = @Schema(implementation = Error400Response.class))),
            @ApiResponse(responseCode = "401", description = "Neautorizovaný interní přístup",
                    content = @Content(schema = @Schema(implementation = Error401Response.class))),
            @ApiResponse(responseCode = "409", description = "Konflikt při zápisu do databáze",
                    content = @Content(schema = @Schema(implementation = Error409Response.class))),
            @ApiResponse(responseCode = "500", description = "Neočekávaná chyba serveru",
                    content = @Content(schema = @Schema(implementation = Error500Response.class)))
    })
    public ResponseEntity<AdminQuestionResponse> saveEmailQuestion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload pro vytvoření emailové otázky",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmailQuestionCreateRequest.class))
            )
            @RequestBody @Valid EmailQuestionCreateRequest request
    ) {
        return ResponseEntity.ok(questionService.saveEmailQuestion(request));
    }

    @PostMapping("/sms")
    @Operation(
            summary = "Uložení SMS otázky",
            description = "Uloží novou SMS otázku do databáze. Kategorie se mapuje přes category_tag nebo legacy category_id.",
            parameters = {
                    @Parameter(name = "X-Internal-Api-Key", in = ParameterIn.HEADER, required = true,
                            description = "Interní API klíč pro přístup na admin endpointy")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Otázka byla úspěšně uložena",
                    content = @Content(schema = @Schema(implementation = AdminQuestionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Neplatný request (validace, neplatná difficulty nebo kategorie)",
                    content = @Content(schema = @Schema(implementation = Error400Response.class))),
            @ApiResponse(responseCode = "401", description = "Neautorizovaný interní přístup",
                    content = @Content(schema = @Schema(implementation = Error401Response.class))),
            @ApiResponse(responseCode = "409", description = "Konflikt při zápisu do databáze",
                    content = @Content(schema = @Schema(implementation = Error409Response.class))),
            @ApiResponse(responseCode = "500", description = "Neočekávaná chyba serveru",
                    content = @Content(schema = @Schema(implementation = Error500Response.class)))
    })
    public ResponseEntity<AdminQuestionResponse> saveSmsQuestion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload pro vytvoření SMS otázky",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SmsQuestionCreateRequest.class))
            )
            @RequestBody @Valid SmsQuestionCreateRequest request
    ) {
        return ResponseEntity.ok(questionService.saveSmsQuestion(request));
    }
}

