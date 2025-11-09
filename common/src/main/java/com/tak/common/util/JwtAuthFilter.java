package com.tak.common.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final Set<String> PUBLIC_PREFIXES;

    public JwtAuthFilter(Set<String> publicPrefixes) {
        this.PUBLIC_PREFIXES = publicPrefixes;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = resolveBearerToken(header);


        // 1) 프리플라이트는 패스
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) 공개 경로는 패스
        if (isPublic(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String id = TokenUtil.validateAccessTokenAndGetID(token);
        //id가 null이면 토큰이 유효하지 않은 것으로 간주하고 필터 체인 계속 진행하지 않음
        if(id == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰이 유효한 경우, request에 userId 속성 추가
        request.setAttribute("userId", id);
        filterChain.doFilter(request,response);
    }

    private String resolveBearerToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7).trim();
        }
        return null;
    }

    private boolean isPublic(HttpServletRequest req) {
        String uri = req.getRequestURI();
        for (String p : PUBLIC_PREFIXES) {
            if (uri.startsWith(p)) return true;
        }
        return false;
    }
}