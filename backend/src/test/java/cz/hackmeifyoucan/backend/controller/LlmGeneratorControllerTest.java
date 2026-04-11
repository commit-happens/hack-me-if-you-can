package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import cz.hackmeifyoucan.backend.dto.llm.LlmGenerateEmailQuestionResponse;
import cz.hackmeifyoucan.backend.llm.GeminiLlmClient;
import cz.hackmeifyoucan.backend.service.PhishingCategoryTagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminLlmGeneratorController.class)
@ActiveProfiles("test")
class LlmGeneratorControllerTest {

    private static final String INTERNAL_API_HEADER = "X-Internal-Api-Key";
    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.internal-api-key}")
    private String internalApiKey;

    @MockitoBean
    private GeminiLlmClient llmClient;

    @MockitoBean
    private PhishingCategoryTagService categoryTagService;

    @Test
    void given_missing_internal_api_key_when_generating_draft_then_return_401_unauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/llm/generate-question")
                        .param("platform", "EMAIL")
                        .param("category", "URGENT")
                        .param("difficulty", "HARD")
                        .param("language", "cs"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));

        verifyNoInteractions(llmClient);
    }

    @Test
    void given_valid_request_when_generating_draft_then_return_llm_json() throws Exception {
        LlmGenerateEmailQuestionResponse response = new LlmGenerateEmailQuestionResponse(
                "Urgent account verification",
                "security@acme.com",
                "Please verify your account",
                "Nátlak na akci",
                "URGENT",
                "HARD",
                true
        );

        when(categoryTagService.resolveTag("urgent"))
                .thenReturn(new PhishingCategoryTagService.ResolvedCategory(3L, "URGENT"));
        when(llmClient.generate(PlatformType.EMAIL, "URGENT", "HARD", "cs")).thenReturn(response);

        mockMvc.perform(get("/api/admin/llm/generate-question")
                        .header(INTERNAL_API_HEADER, internalApiKey)
                        .param("platform", "EMAIL")
                        .param("category", "urgent")
                        .param("difficulty", "HARD")
                        .param("language", "cs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.subject").value("Urgent account verification"))
                .andExpect(jsonPath("$.sender").value("security@acme.com"))
                .andExpect(jsonPath("$.category").value("URGENT"))
                .andExpect(jsonPath("$.difficulty").value("HARD"))
                .andExpect(jsonPath("$.is_phishing").value(true));
    }

    @Test
    void given_invalid_category_when_generating_draft_then_return_400_bad_request() throws Exception {
        when(categoryTagService.resolveTag("random_kategorie_blabla"))
                .thenThrow(new IllegalArgumentException("Neplatná kategorie: random_kategorie_blabla. Povolené hodnoty: [FAKE_URL, LEGIT]"));

        mockMvc.perform(get("/api/admin/llm/generate-question")
                        .header(INTERNAL_API_HEADER, internalApiKey)
                        .param("platform", "EMAIL")
                        .param("category", "random_kategorie_blabla")
                        .param("difficulty", "HARD")
                        .param("language", "cs"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value(allOf(
                        containsString("random_kategorie_blabla"),
                        containsString("Povolen"),
                        containsString("LEGIT"),
                        containsString("FAKE_URL")
                )));

        verifyNoInteractions(llmClient);
    }
}
