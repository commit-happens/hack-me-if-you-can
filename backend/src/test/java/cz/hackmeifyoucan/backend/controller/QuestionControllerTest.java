package cz.hackmeifyoucan.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cz.hackmeifyoucan.backend.dto.QuestionResponse;
import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.service.QuestionService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    private static final String QUESTIONS_API = "/questions/random";
    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;

    @Nested
    class GetRandomQuestionTests {

        @Test
        void given_questions_exist_when_getting_random_questions_then_return_response_fields() throws Exception {
            // Given
            QuestionResponse first = new QuestionResponse(
                    35L,
                    "email",
                    Map.of("sender", "security@acme.com", "subject", "Urgent account verification"),
                    "Please verify your account immediately",
                    "Podvodny email tlaci na rychlou akci."
            );

            QuestionResponse second = new QuestionResponse(
                    36L,
                    "sms",
                    Map.of("sender", "Bank", "subject", "Payment pending"),
                    "Confirm payment at this link",
                    "SMS obsahuje podezrely odkaz."
            );

            when(questionService.getRandomQuestionsByDifficulty(1, 2)).thenReturn(List.of(first, second));

            // When & Then
            mockMvc.perform(get(QUESTIONS_API)
                            .param("difficulty", "EASY")
                            .param("limit", "2"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].id").value(35))
                    .andExpect(jsonPath("$[0].platform").value("email"))
                    .andExpect(jsonPath("$[0].metadata.sender").value("security@acme.com"))
                    .andExpect(jsonPath("$[0].metadata.subject").value("Urgent account verification"))
                    .andExpect(jsonPath("$[0].content").value("Please verify your account immediately"))
                    .andExpect(jsonPath("$[0].explanation").value("Podvodny email tlaci na rychlou akci."));
        }

        @Test
        void given_no_questions_when_getting_random_questions_then_return_empty_list() throws Exception {
            // Given
            when(questionService.getRandomQuestionsByDifficulty(1, 3)).thenReturn(List.of());

            // When & Then
            mockMvc.perform(get(QUESTIONS_API)
                            .param("difficulty", "EASY")
                            .param("limit", "3"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(0));
        }

        @Test
        void given_service_throws_exception_when_getting_random_questions_then_return_500_error() throws Exception {
            // Given
            when(questionService.getRandomQuestionsByDifficulty(1, 1))
                    .thenThrow(new RuntimeException("Chyba (napr. databaze)"));

            // When & Then
            mockMvc.perform(get(QUESTIONS_API)
                            .param("difficulty", "EASY")
                            .param("limit", "1"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").value("Neočekávaná chyba serveru"));
        }
    }

    @Nested
    class ValidationTests {

        @Test
        void given_limit_zero_when_getting_random_questions_then_return_400_error() throws Exception {
            // When & Then
            mockMvc.perform(get(QUESTIONS_API)
                            .param("difficulty", "EASY")
                            .param("limit", "0"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Limit musí být kladné číslo"));
        }

        @Test
        void given_negative_limit_when_getting_random_questions_then_return_400_error() throws Exception {
            // When & Then
            mockMvc.perform(get(QUESTIONS_API)
                            .param("difficulty", "EASY")
                            .param("limit", "-5"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Limit musí být kladné číslo"));
        }

        @Test
        void given_limit_exceeds_maximum_when_getting_random_questions_then_return_400_error() throws Exception {
            // When & Then
            mockMvc.perform(get(QUESTIONS_API)
                            .param("difficulty", "EASY")
                            .param("limit", "101"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Limit nesmí překročit 100"));
        }

        @Test
        void given_limit_at_maximum_when_getting_random_questions_then_service_is_called() throws Exception {
            // Given
            when(questionService.getRandomQuestionsByDifficulty(1, 100)).thenReturn(List.of());

            // When & Then
            mockMvc.perform(get(QUESTIONS_API)
                            .param("difficulty", "EASY")
                            .param("limit", "100"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(0));
        }

        @Test
        void given_all_difficulty_levels_when_getting_random_questions_then_service_is_called() throws Exception {
            // Given
            when(questionService.getRandomQuestionsByDifficulty(1, 5)).thenReturn(List.of());
            when(questionService.getRandomQuestionsByDifficulty(2, 5)).thenReturn(List.of());
            when(questionService.getRandomQuestionsByDifficulty(3, 5)).thenReturn(List.of());

            // When & Then - test all difficulty enum values (EASY, MEDIUM, HARD)
            for (Difficulty difficulty : Difficulty.values()) {
                mockMvc.perform(get(QUESTIONS_API)
                                .param("difficulty", difficulty.name())
                                .param("limit", "5"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.size()").value(0));
            }
        }

        @Test
        void given_invalid_difficulty_when_getting_random_questions_then_return_400_error() throws Exception {
            // When & Then
            mockMvc.perform(get(QUESTIONS_API)
                            .param("difficulty", "INVALID")
                            .param("limit", "5"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Neplatná hodnota parametru: difficulty"));
        }
    }
}

