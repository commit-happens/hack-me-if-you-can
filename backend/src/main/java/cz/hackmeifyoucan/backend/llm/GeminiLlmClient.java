package cz.hackmeifyoucan.backend.llm;

import cz.hackmeifyoucan.backend.dto.LlmGenerateEmailQuestionResponse;
import cz.hackmeifyoucan.backend.dto.LlmGenerateSmsQuestionResponse;
import cz.hackmeifyoucan.backend.enums.PlatformType;
import cz.hackmeifyoucan.backend.llm.prompt.EmailPromptTemplate;
import cz.hackmeifyoucan.backend.llm.prompt.SmsPromptTemplate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GeminiLlmClient {

    private final ChatClient chatClient;
    private final EmailPromptTemplate emailPromptTemplate;
    private final SmsPromptTemplate smsPromptTemplate;

    public GeminiLlmClient(
            ChatClient.Builder builder,
            EmailPromptTemplate emailPromptTemplate,
            SmsPromptTemplate smsPromptTemplate
    ) {
        this.chatClient = builder.build();
        this.emailPromptTemplate = emailPromptTemplate;
        this.smsPromptTemplate = smsPromptTemplate;
    }

    public Object generate(PlatformType platform, String category, String difficulty, String language) {
        validateInputs(platform, category, difficulty, language);

        String resolvedPrompt = switch (platform) {
            case EMAIL -> emailPromptTemplate.render(category, difficulty, language);
            case SMS -> smsPromptTemplate.render(category, difficulty, language);
        };
        Class<?> targetClass = switch (platform) {
            case EMAIL -> LlmGenerateEmailQuestionResponse.class;
            case SMS -> LlmGenerateSmsQuestionResponse.class;
        };

        return chatClient.prompt()
                .system(resolvedPrompt)
                .user("Vygeneruj JSON podle instrukci v systemovem promptu.")
                .call()
                .entity(targetClass);
    }


    private void validateInputs(PlatformType platform, String category, String difficulty, String language) {
        if (platform == null) {
            throw new IllegalArgumentException("platform must not be null");
        }
        if (!StringUtils.hasText(category)) {
            throw new IllegalArgumentException("category must not be blank");
        }
        if (!StringUtils.hasText(difficulty)) {
            throw new IllegalArgumentException("difficulty must not be blank");
        }
        if (!StringUtils.hasText(language)) {
            throw new IllegalArgumentException("language must not be blank");
        }
    }
}