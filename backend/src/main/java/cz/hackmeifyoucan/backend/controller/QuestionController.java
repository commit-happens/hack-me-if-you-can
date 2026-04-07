package cz.hackmeifyoucan.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.hackmeifyoucan.backend.dto.Error400Response;
import cz.hackmeifyoucan.backend.dto.Error500Response;
import cz.hackmeifyoucan.backend.dto.QuestionResponse;
import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.exception.InvalidQuestionParameterException;
import cz.hackmeifyoucan.backend.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/questions")
@Tag(name = "question-controller", description = "Endpointy pro správu otázek - čtení otázek podle obtížnosti a limitu")
public class QuestionController {

    private static final int REQUEST_LIMIT = 100;

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/random")
    @Operation(
            summary = "Získat náhodné otázky podle obtížnosti",
            description = "Vrátí seznam náhodných phishingových otázek filtrovaných podle obtížnosti. " +
                    "Limit je omezen na maximálně " + REQUEST_LIMIT + " otázek. " +
                    "Pokud pro danou obtížnost neexistují otázky, vrátí se prázdný seznam."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Úspěšně vráceny náhodné otázky",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = QuestionResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Chybný požadavek - invalidní limit (limit <= 0 nebo limit > " + REQUEST_LIMIT + ")",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Error400Response.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Interní chyba serveru - selhání databáze nebo jiná neočekávaná chyba",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Error500Response.class)
                    )
            )
    })
    public ResponseEntity<List<QuestionResponse>> getRandomQuestionsByDifficulty(
            @Parameter(
                    name = "difficulty",
                    description = "Úroveň obtížnosti otázek. Validní hodnoty: EASY, MEDIUM, HARD. Pokud obtížnost neexistuje, vrátí se prázdný seznam.",
                    example = "EASY"
            )
            @RequestParam Difficulty difficulty,

            @Parameter(
                    name = "limit",
                    description = "Maximální počet otázek k vrácení. Musí být kladné číslo a max " + REQUEST_LIMIT + ".",
                    example = "10"
            )
            @RequestParam int limit
    ) {
        validateLimit(limit);
        return ResponseEntity.ok(questionService.getRandomQuestionsByDifficulty(difficulty, limit));
    }


    private void validateLimit(int limit) {
        if (limit <= 0) {
            throw new InvalidQuestionParameterException("Limit musí být kladné číslo");
        }
        if (limit > REQUEST_LIMIT) {
            throw new InvalidQuestionParameterException("Limit nesmí překročit " + REQUEST_LIMIT);
        }
    }
}
