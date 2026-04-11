package cz.hackmeifyoucan.backend.llm;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import cz.hackmeifyoucan.backend.dto.llm.LlmGenerateEmailQuestionResponse;
import cz.hackmeifyoucan.backend.dto.llm.LlmGenerateQuestionResponse;
import cz.hackmeifyoucan.backend.dto.llm.LlmGenerateSmsQuestionResponse;
import cz.hackmeifyoucan.backend.llm.prompt.PromptTemplateResolver;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GeminiLlmClient {

    private final PromptTemplateResolver promptTemplateResolver;
    private final ChatClient chatClient;

    public GeminiLlmClient(ChatClient.Builder builder, PromptTemplateResolver promptTemplateResolver) {
        this.promptTemplateResolver = promptTemplateResolver;
        this.chatClient = builder.build();
    }

    public LlmGenerateQuestionResponse generate(PlatformType platform, String category, String difficulty, String language) {
        validateInputs(platform, category, difficulty, language);

        String resolvedPrompt = promptTemplateResolver.resolve(platform).render(category, difficulty, language);
        Class<? extends LlmGenerateQuestionResponse> targetClass = switch (platform) {
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