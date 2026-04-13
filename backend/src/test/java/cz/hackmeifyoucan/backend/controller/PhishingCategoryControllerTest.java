package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.dto.PhishingCategoryResponse;
import cz.hackmeifyoucan.backend.exception.PhishingCategoryNotFoundException;
import cz.hackmeifyoucan.backend.service.PhishingCategoryService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhishingCategoryController.class)
class PhishingCategoryControllerTest {

    private static final String API = "/phishing-categories";
    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhishingCategoryService phishingCategoryService;

    @Nested
    class GetAllPhishingCategoriesTests {

        @Test
        void given_categories_exist_when_getting_all_categories_then_return_list() throws Exception {
            PhishingCategoryResponse first = new PhishingCategoryResponse(
                    1L,
                    "FAKE_URL",
                    "Útoky využívající podvržené odkazy."
            );
            PhishingCategoryResponse second = new PhishingCategoryResponse(
                    2L,
                    "URGENT",
                    "Nátlak na okamžitou akci."
            );

            when(phishingCategoryService.getAllCategories()).thenReturn(List.of(first, second));

            mockMvc.perform(get(API))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].tag").value("FAKE_URL"))
                    .andExpect(jsonPath("$[0].description").value("Útoky využívající podvržené odkazy."))
                    .andExpect(jsonPath("$[1].tag").value("URGENT"));
        }
    }

    @Nested
    class GetPhishingCategoryByTagTests {

        @Test
        void given_existing_tag_when_getting_category_then_return_detail() throws Exception {
            PhishingCategoryResponse response = new PhishingCategoryResponse(
                    3L,
                    "FAKE_DOC",
                    "Podvodné zprávy s přílohami."
            );
            when(phishingCategoryService.getCategoryByTag("FAKE_DOC")).thenReturn(response);

            mockMvc.perform(get(API + "/{tag}", "FAKE_DOC"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(3))
                    .andExpect(jsonPath("$.tag").value("FAKE_DOC"))
                    .andExpect(jsonPath("$.description").value("Podvodné zprávy s přílohami."));
        }

        @Test
        void given_lowercase_tag_when_getting_category_then_service_accepts_case_insensitive_tag() throws Exception {
            PhishingCategoryResponse response = new PhishingCategoryResponse(
                    4L,
                    "CRED_THEFT",
                    "Podvodné přihlašovací stránky."
            );
            when(phishingCategoryService.getCategoryByTag("cred_theft")).thenReturn(response);

            mockMvc.perform(get(API + "/{tag}", "cred_theft"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(4))
                    .andExpect(jsonPath("$.tag").value("CRED_THEFT"));
        }

        @Test
        void given_non_existing_tag_when_getting_category_then_return_404() throws Exception {
            when(phishingCategoryService.getCategoryByTag("UNKNOWN"))
                    .thenThrow(new PhishingCategoryNotFoundException("UNKNOWN"));

            mockMvc.perform(get(API + "/{tag}", "UNKNOWN"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.error").value("Phishing category not found for tag: UNKNOWN"));
        }

        @Test
        void given_service_failure_when_getting_category_then_return_500() throws Exception {
            when(phishingCategoryService.getCategoryByTag("FAKE_URL"))
                    .thenThrow(new RuntimeException("Neocekavana chyba"));

            mockMvc.perform(get(API + "/{tag}", "FAKE_URL"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").value("Neočekávaná chyba serveru"));
        }
    }
}





