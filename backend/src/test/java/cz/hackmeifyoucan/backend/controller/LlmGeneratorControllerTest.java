package cz.hackmeifyoucan.backend.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cz.hackmeifyoucan.backend.dto.LlmGenerateEmailQuestionResponse;
import cz.hackmeifyoucan.backend.dto.LlmGenerateSmsQuestionResponse;
import cz.hackmeifyoucan.backend.dto.PhishingCategoryResponse;
import cz.hackmeifyoucan.backend.exception.PhishingCategoryNotFoundException;
import cz.hackmeifyoucan.backend.llm.GeminiLlmClient;
import cz.hackmeifyoucan.backend.enums.PlatformType;
import cz.hackmeifyoucan.backend.service.PhishingCategoryService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LlmGeneratorController.class)
class LlmGeneratorControllerTest {

    private static final String API = "/llm/generate-question";
    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeminiLlmClient llmClient;

    @MockitoBean
    private PhishingCategoryService phishingCategoryService;

    @Nested
    class GenerateDraftTests {

        @Test
        void given_valid_request_when_generating_email_question_then_return_generated_payload() throws Exception {
            PhishingCategoryResponse category = new PhishingCategoryResponse(1L, "FAKE_URL", "Fake URL popis");
            var response = new LlmGenerateEmailQuestionResponse(
                    "Urgent update",
                    "security@acme.com",
                    "Please verify your account",
                    "Obsah vypadá jako phishing",
                    "FAKE_URL",
                    "HARD",
                    true
            );

            when(phishingCategoryService.getCategoryByTag("fake_url")).thenReturn(category);
            when(llmClient.generate(PlatformType.EMAIL, "FAKE_URL", "HARD", "en")).thenReturn(response);

            mockMvc.perform(get(API)
                            .param("platform", "EMAIL")
                            .param("category", "fake_url")
                            .param("difficulty", "HARD")
                            .param("language", "en"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.subject").value("Urgent update"))
                    .andExpect(jsonPath("$.sender").value("security@acme.com"))
                    .andExpect(jsonPath("$.content").value("Please verify your account"))
                    .andExpect(jsonPath("$.category").value("FAKE_URL"))
                    .andExpect(jsonPath("$.difficulty").value("HARD"))
                    .andExpect(jsonPath("$.is_phishing").value(true));

            verify(phishingCategoryService).getCategoryByTag("fake_url");
            verify(llmClient).generate(cz.hackmeifyoucan.backend.enums.PlatformType.EMAIL, "FAKE_URL", "HARD", "en");
        }

        @Test
        void given_missing_language_when_generating_sms_question_then_use_default_language() throws Exception {
            PhishingCategoryResponse category = new PhishingCategoryResponse(2L, "URGENT", "Urgent popis");
            var response = new LlmGenerateSmsQuestionResponse(
                    "Bank",
                    "+420123456789",
                    "Confirm your payment",
                    "SMS vypadá podezřele",
                    "URGENT",
                    "EASY",
                    true
            );

            when(phishingCategoryService.getCategoryByTag("URGENT")).thenReturn(category);
            when(llmClient.generate(PlatformType.SMS, "URGENT", "EASY", "cs")).thenReturn(response);

            mockMvc.perform(get(API)
                            .param("platform", "SMS")
                            .param("category", "URGENT")
                            .param("difficulty", "EASY"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.sender").value("Bank"))
                    .andExpect(jsonPath("$.phoneNumber").value("+420123456789"))
                    .andExpect(jsonPath("$.content").value("Confirm your payment"))
                    .andExpect(jsonPath("$.category").value("URGENT"))
                    .andExpect(jsonPath("$.difficulty").value("EASY"))
                    .andExpect(jsonPath("$.is_phishing").value(true));

            verify(phishingCategoryService).getCategoryByTag("URGENT");
            verify(llmClient).generate(cz.hackmeifyoucan.backend.enums.PlatformType.SMS, "URGENT", "EASY", "cs");
        }

        @Test
        void given_unknown_category_when_generating_question_then_return_404() throws Exception {
            when(phishingCategoryService.getCategoryByTag("UNKNOWN"))
                    .thenThrow(new PhishingCategoryNotFoundException("UNKNOWN"));

            mockMvc.perform(get(API)
                            .param("platform", "EMAIL")
                            .param("category", "UNKNOWN")
                            .param("difficulty", "MEDIUM"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("Phishing category not found for tag: UNKNOWN"));

            verify(phishingCategoryService).getCategoryByTag("UNKNOWN");
            verifyNoInteractions(llmClient);
        }

        @Test
        void given_missing_category_when_generating_question_then_return_400_and_skip_services() throws Exception {
            mockMvc.perform(get(API)
                            .param("platform", "EMAIL")
                            .param("difficulty", "MEDIUM"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Neplatná data v požadavku"));

            verifyNoInteractions(phishingCategoryService, llmClient);
        }
    }
}


