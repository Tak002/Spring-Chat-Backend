package com.tak.app_auth.util;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    //todo 이 키는 반드시 외부에 노출되면 안됩니다. 환경변수나 안전한 저장소에서 관리하세요.
    private final Key signingKey = Keys.hmacShaKeyFor(
            "THIS_IS_DEMO_SECRET_KEY_CHANGE_ME_32_BYTES_MIN".getBytes()
    );

    // 1시간짜리 토큰 예시 (ms 단위)
    private final long EXPIRATION_MS = 60 * 60 * 1000;

    // 토큰 만들기: userId 같은 걸 subject로 넣는 패턴이 제일 기본
    public String generateToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);
        return Jwts.builder()
                .subject(userId)          // 내가 식별하고 싶은 값 (ex: 유저 PK)
                .issuedAt(now)            // 발급 시각 (iat)
                .expiration(expiry)       // 만료 시각 (exp)
                .signWith(signingKey)
                .compact();
    }

    // 토큰 파싱해서 클레임(내용물) 가져오기
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) signingKey) // 서명 검증
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰 안에 넣어둔 subject (여기서는 userId) 꺼내기
    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰 만료 여부/서명 여부 등 기본 유효성
    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            Date exp = claims.getExpiration();
            return exp.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

