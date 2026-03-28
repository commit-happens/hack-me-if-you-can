package cz.hackmeifyoucan.backend.controller;

import cz.hackmeifyoucan.backend.dto.AnswerRequest;
import cz.hackmeifyoucan.backend.dto.AnswerResponse;
import cz.hackmeifyoucan.backend.exception.DuplicateAnswerException;
import cz.hackmeifyoucan.backend.exception.PlayerNotFoundException;
import cz.hackmeifyoucan.backend.service.AnswerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnswerController.class)
class AnswerControllerTest {

    private static final String ANSWERS_API = "/answers";
    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnswerService answerService;

    @Test
    void given_valid_answer_request_when_submitting_answer_then_return_score_response() throws Exception {
        // Given
        AnswerRequest request = new AnswerRequest(1L, 3L, "session-1", false, 10);
        AnswerResponse response = new AnswerResponse(true, 395);
        when(answerService.submitAnswer(request)).thenReturn(response);

        // When & Then
        mockMvc.perform(post(ANSWERS_API)
                        .contentType(APPLICATION_JSON)
                        .content("{\"player_id\":1,\"question_id\":3,\"session_id\":\"session-1\",\"is_phishing\":false,\"remain_time\":10}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.answer_correct").value(true))
                .andExpect(jsonPath("$.score").value(395));
    }

    @Test
    void given_invalid_answer_request_when_submitting_answer_then_return_400_bad_request() throws Exception {
        // When & Then
        mockMvc.perform(post(ANSWERS_API)
                        .contentType(APPLICATION_JSON)
                        .content("{\"player_id\":1,\"question_id\":3,\"session_id\":\"session-1\",\"is_phishing\":false,\"remain_time\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Neplatná data v požadavku"));
    }

    @Test
    void given_missing_player_when_submitting_answer_then_return_404_not_found() throws Exception {
        // Given
        AnswerRequest request = new AnswerRequest(999L, 3L, "session-1", false, 10);
        when(answerService.submitAnswer(request)).thenThrow(new PlayerNotFoundException(999L));

        // When & Then
        mockMvc.perform(post(ANSWERS_API)
                        .contentType(APPLICATION_JSON)
                        .content("{\"player_id\":999,\"question_id\":3,\"session_id\":\"session-1\",\"is_phishing\":false,\"remain_time\":10}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Hráč nenalezen pro ID: 999"));
    }

    @Test
    void given_duplicate_answer_when_submitting_answer_then_return_409_conflict() throws Exception {
        // Given
        AnswerRequest request = new AnswerRequest(1L, 3L, "session-1", false, 10);
        when(answerService.submitAnswer(request)).thenThrow(new DuplicateAnswerException(1L, 3L, "session-1"));

        // When & Then
        mockMvc.perform(post(ANSWERS_API)
                        .contentType(APPLICATION_JSON)
                        .content("{\"player_id\":1,\"question_id\":3,\"session_id\":\"session-1\",\"is_phishing\":false,\"remain_time\":10}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void given_too_long_session_id_when_submitting_answer_then_return_400_bad_request() throws Exception {
        String longSessionId = "a".repeat(65);
        String invalidJson = "{\"player_id\":1,\"question_id\":3,\"session_id\":\"" + longSessionId + "\",\"is_phishing\":false,\"remain_time\":10}";

        mockMvc.perform(post(ANSWERS_API)
                        .contentType(APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Neplatná data v požadavku"))
                .andExpect(jsonPath("$.fields.sessionId").value("session_id nesmí překročit 64 znaků"));

        verifyNoInteractions(answerService);
    }

    @Test
    void given_too_large_remain_time_when_submitting_answer_then_return_400_bad_request() throws Exception {
        mockMvc.perform(post(ANSWERS_API)
                        .contentType(APPLICATION_JSON)
                        .content("{\"player_id\":1,\"question_id\":3,\"session_id\":\"session-1\",\"is_phishing\":false,\"remain_time\":61}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Neplatná data v požadavku"))
                .andExpect(jsonPath("$.fields.remainTime").value("remain_time musí být <= 60"));

        verifyNoInteractions(answerService);
    }
}
