package com.tak.app_service.config;

import com.tak.common.util.JwtAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Set;

@Configuration
public class FilterConfig {
    private static final Set<String> publicPrefixes =Set.of(
        "/auth/login",
        "/auth/signup"
    );

    @Bean
    public OncePerRequestFilter jwtAuthFilter() {
        return new JwtAuthFilter(publicPrefixes);
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> jwtAuthFilterRegistration(
            OncePerRequestFilter jwtAuthFilter) {

        FilterRegistrationBean<OncePerRequestFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(jwtAuthFilter);
        reg.addUrlPatterns("/*");           // 적용 범위 (모든 경로에 필터 적용)
        reg.setName("jwtAuthFilter");
        return reg;
    }
}
