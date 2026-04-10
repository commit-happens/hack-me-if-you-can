package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.dto.AnswerRequest;
import cz.hackmeifyoucan.backend.dto.AnswerResponse;
import cz.hackmeifyoucan.backend.dto.Error400Response;
import cz.hackmeifyoucan.backend.dto.Error404Response;
import cz.hackmeifyoucan.backend.dto.Error409Response;
import cz.hackmeifyoucan.backend.dto.Error500Response;
import cz.hackmeifyoucan.backend.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/answers")
@Tag(name = "answer-controller", description = "Endpoint pro vyhodnocení odpovědi hráče")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @Operation(
            summary = "Vyhodnotit odpověď hráče",
            description = "Spočítá odměnu, uloží auditní záznam odpovědi a aktualizuje score hráče."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odpověď byla vyhodnocena",
                    content = @Content(schema = @Schema(implementation = AnswerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Neplatná data v požadavku",
                    content = @Content(schema = @Schema(implementation = Error400Response.class))),
            @ApiResponse(responseCode = "404", description = "Hráč nebo otázka nebyli nalezeni",
                    content = @Content(schema = @Schema(implementation = Error404Response.class))),
            @ApiResponse(responseCode = "409", description = "Odpověď pro hráče a otázku pro daný sessionId již existuje",
                    content = @Content(schema = @Schema(implementation = Error409Response.class))),
            @ApiResponse(responseCode = "500", description = "Neočekávaná chyba serveru",
                    content = @Content(schema = @Schema(implementation = Error500Response.class)))
    })
    @PostMapping
    public ResponseEntity<AnswerResponse> submitAnswer(@RequestBody @Valid AnswerRequest request) {
        return ResponseEntity.ok(answerService.submitAnswer(request));
    }
}

