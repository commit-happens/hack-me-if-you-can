package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.service.PhishingCategoryService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminPhishingCategoryController.class)
@ActiveProfiles("test")
class AdminPhishingCategoryControllerTest {

    private static final String INTERNAL_API_HEADER = "X-Internal-Api-Key";

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.internal-api-key}")
    private String internalApiKey;

    @MockitoBean
    private PhishingCategoryService phishingCategoryService;

    @MockitoBean
    private PhishingCategoryTagService categoryTagService;

    @Test
    void given_missing_internal_api_key_when_resolving_category_then_return_401_unauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/categories/resolve").param("tag", "urgent"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));

        verifyNoInteractions(categoryTagService);
    }

    @Test
    void given_lowercase_tag_when_resolving_category_then_return_canonical_category() throws Exception {
        when(categoryTagService.resolveTag("urgent"))
                .thenReturn(new PhishingCategoryTagService.ResolvedCategory(3L, "URGENT"));

        mockMvc.perform(get("/api/admin/categories/resolve")
                        .header(INTERNAL_API_HEADER, internalApiKey)
                        .param("tag", "urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.tag").value("URGENT"));
    }

    @Test
    void given_invalid_tag_when_resolving_category_then_return_400_bad_request() throws Exception {
        when(categoryTagService.resolveTag("xyz"))
                .thenThrow(new IllegalArgumentException("Neplatná kategorie: xyz. Povolené hodnoty: [FAKE_URL, LEGIT]"));

        mockMvc.perform(get("/api/admin/categories/resolve")
                        .header(INTERNAL_API_HEADER, internalApiKey)
                        .param("tag", "xyz"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Neplatná kategorie: xyz. Povolené hodnoty: [FAKE_URL, LEGIT]"));
    }
}

