package com.tak.chat_history;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://127.0.0.1:8080")
                .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
