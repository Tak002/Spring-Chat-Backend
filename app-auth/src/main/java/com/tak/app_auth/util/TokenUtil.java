package com.tak.app_auth.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class TokenUtil {
    public static final long REFRESH_TOKEN_VALIDITY_Days = 10; // 10일

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
