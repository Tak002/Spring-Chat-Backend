package com.tak.app_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Set;

@Configuration
public class FilterConfig {
    @Bean
    public OncePerRequestFilter jwtAuthFilter() {
        return new com.tak.common.util.JwtAuthFilter(
                Set.of(
                    "/auth/login",
                    "/auth/signup",
                    "/auth/verify-email"
            )
        );
    }
}
