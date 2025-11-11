package com.tak.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class TokenUtil {
    public static final long REFRESH_TOKEN_VALIDITY_Days = 1; // 1일
    private static final long ACCESS_TOKEN_EXPIRATION_MS = 10 * 60 * 1000; //10분

    //todo 이 키는 반드시 외부에 노출되면 안됩니다. 환경변수나 안전한 저장소에서 관리하세요.
    private static final Key signingKey = Keys.hmacShaKeyFor(
            "THIS_IS_DEMO_SECRET_KEY_CHANGE_ME_32_BYTES_MIN".getBytes()
    );

    // 토큰 만들기: userId 같은 걸 subject로 넣는 패턴이 제일 기본
    public static String generateAccessToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_MS);
        return Jwts.builder()
                .subject(userId)          // 내가 식별하고 싶은 값 (ex: 유저 PK)
                .issuedAt(now)            // 발급 시각 (iat)
                .expiration(expiry)       // 만료 시각 (exp)
                .signWith(signingKey)
                .compact();
    }

    // 토큰 파싱해서 클레임(내용물) 가져오기
    private static Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) signingKey) // 서명 검증
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰 만료 여부/서명 여부 등 기본 유효성
    public static String validateAccessTokenAndGetID(String token) {
        try {
            Claims claims = parseClaims(token);
            Date exp = claims.getExpiration();
            if(exp.after(new Date())){
                return claims.getSubject();
            }
            return null;
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return null;
        }
    }
    // refresh 토큰 원본으로 쓸 랜덤 문자열 생성
    public static String generateRawToken() {
        // 32바이트 = 256비트 랜덤. (원하면 64로 키워도 됨)
        byte[] randomBytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        // URL-safe Base64 (패딩 '=' 제거) -> 쿠키/헤더로 주기 편함
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    // 클라이언트에 준 rawToken → DB에 넣을 hash(64글자 hex)
    public static String sha256Hex(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes); // 64글자 hex
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not supported", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
