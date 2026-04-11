package cz.hackmeifyoucan.backend.llm.prompt;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PromptTemplateResolver {

    private final Map<PlatformType, PromptTemplate> templates;

    public PromptTemplateResolver(List<PromptTemplate> templates) {
        this.templates = new EnumMap<>(PlatformType.class);
        for (PromptTemplate template : templates) {
            PromptTemplate previous = this.templates.put(template.platform(), template);
            if (previous != null) {
                throw new IllegalStateException("Duplicate prompt template for platform: " + template.platform());
            }
        }
    }

    public PromptTemplate resolve(PlatformType platform) {
        PromptTemplate template = templates.get(platform);
        if (template == null) {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
        return template;
    }
}

