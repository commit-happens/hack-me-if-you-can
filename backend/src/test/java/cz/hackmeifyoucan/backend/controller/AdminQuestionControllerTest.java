package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.dto.AdminQuestionResponse;
import cz.hackmeifyoucan.backend.dto.EmailQuestionCreateRequest;
import cz.hackmeifyoucan.backend.dto.SmsQuestionCreateRequest;
import cz.hackmeifyoucan.backend.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminQuestionController.class)
@ActiveProfiles("test")
class AdminQuestionControllerTest {

    private static final String INTERNAL_API_HEADER = "X-Internal-Api-Key";
    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.internal-api-key}")
    private String internalApiKey;

    @MockitoBean
    private QuestionService questionService;

    @Test
    void given_missing_internal_api_key_when_saving_email_question_then_return_401_unauthorized() throws Exception {
        mockMvc.perform(post("/api/admin/questions/email")
                        .contentType(APPLICATION_JSON)
                        .content("{\"subject\":\"Urgent\",\"sender\":\"security@acme.com\",\"content\":\"Body\",\"explanation\":\"Expl\",\"category_id\":1,\"difficulty\":\"HARD\",\"is_phishing\":true}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));

        verifyNoInteractions(questionService);
    }

    @Test
    void given_valid_email_request_when_saving_question_then_return_persisted_dto() throws Exception {
        AdminQuestionResponse response = new AdminQuestionResponse(
                101L,
                "email",
                "HARD",
                3L,
                true,
                Map.of("sender", "security@acme.com", "subject", "Urgent account verification"),
                "Please verify your account immediately",
                "Nátlak na okamžitou akci",
                0,
                LocalDateTime.of(2026, 4, 4, 12, 0)
        );

        when(questionService.saveEmailQuestion(any(EmailQuestionCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/questions/email")
                        .header(INTERNAL_API_HEADER, internalApiKey)
                        .contentType(APPLICATION_JSON)
                        .content("{\"subject\":\"Urgent account verification\",\"sender\":\"security@acme.com\",\"content\":\"Please verify your account immediately\",\"explanation\":\"Nátlak na okamžitou akci\",\"category_tag\":\"URGENT\",\"difficulty\":\"HARD\",\"is_phishing\":true}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.platform").value("email"))
                .andExpect(jsonPath("$.difficulty").value("HARD"))
                .andExpect(jsonPath("$.category_id").value(3))
                .andExpect(jsonPath("$.is_phishing").value(true))
                .andExpect(jsonPath("$.metadata.sender").value("security@acme.com"))
                .andExpect(jsonPath("$.metadata.subject").value("Urgent account verification"))
                .andExpect(jsonPath("$.penalty").value(0));
    }

    @Test
    void given_valid_sms_request_when_saving_question_then_return_persisted_dto() throws Exception {
        AdminQuestionResponse response = new AdminQuestionResponse(
                102L,
                "sms",
                "EASY",
                2L,
                true,
                Map.of("sender", "InfoSMS", "phoneNumber", "+420123456789"),
                "Klikněte na odkaz",
                "Podezřelý odkaz",
                0,
                LocalDateTime.of(2026, 4, 4, 12, 30)
        );

        when(questionService.saveSmsQuestion(any(SmsQuestionCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/questions/sms")
                        .header(INTERNAL_API_HEADER, internalApiKey)
                        .contentType(APPLICATION_JSON)
                        .content("{\"sender\":\"InfoSMS\",\"phoneNumber\":\"+420123456789\",\"content\":\"Klikněte na odkaz\",\"explanation\":\"Podezřelý odkaz\",\"category_tag\":\"FAKE_URL\",\"difficulty\":\"EASY\",\"is_phishing\":true}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(102))
                .andExpect(jsonPath("$.platform").value("sms"))
                .andExpect(jsonPath("$.difficulty").value("EASY"))
                .andExpect(jsonPath("$.category_id").value(2))
                .andExpect(jsonPath("$.is_phishing").value(true))
                .andExpect(jsonPath("$.metadata.sender").value("InfoSMS"))
                .andExpect(jsonPath("$.metadata.phoneNumber").value("+420123456789"))
                .andExpect(jsonPath("$.penalty").value(0));
    }
}



