package cz.hackmeifyoucan.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * Ověřuje hlavičku X-Internal-Api-Key pro chráněné admin requesty.
 * Pokud klíč nesouhlasí, request zastaví a vrátí 401 s JSON chybou.
 */
@Component
public class InternalApiKeyInterceptor implements HandlerInterceptor {

    public static final String API_KEY_HEADER = "X-Internal-Api-Key";

    private final String expectedApiKey;
    private final ObjectMapper objectMapper;

    public InternalApiKeyInterceptor(
        @Value("${app.internal-api-key}") String expectedApiKey,
        ObjectMapper objectMapper
    ) {
        this.expectedApiKey = expectedApiKey;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler
    ) throws Exception {

        String providedApiKey = request.getHeader(API_KEY_HEADER);

        if (expectedApiKey != null && expectedApiKey.equals(providedApiKey)) {
            return true;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8"); // Ensure Czech characters render correctly

        Map<String, Object> errorDetails = Map.of(
            "status", HttpServletResponse.SC_UNAUTHORIZED,
            "error", "Unauthorized",
            "message", "Neautorizovaný interní přístup",
            "path", request.getRequestURI()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
        return false;
    }
}

