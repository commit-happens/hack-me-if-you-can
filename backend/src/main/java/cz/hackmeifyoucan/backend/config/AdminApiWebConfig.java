package cz.hackmeifyoucan.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Připojuje kontrolu interního API klíče na všechny endpointy pod /api/admin/**,
 * aby se k admin operacím dostaly jen interní nástroje se správnou hlavičkou.
 */
@Configuration
@RequiredArgsConstructor
public class AdminApiWebConfig implements WebMvcConfigurer {

    private final InternalApiKeyInterceptor internalApiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(internalApiKeyInterceptor)
            .addPathPatterns("/api/admin/**")
            .excludePathPatterns("/api/admin/v3/api-docs/**");
    }
}