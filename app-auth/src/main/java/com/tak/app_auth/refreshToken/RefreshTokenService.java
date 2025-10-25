package com.tak.app_auth.refreshToken;

import com.tak.app_auth.appUser.AppUser;
import com.tak.app_auth.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    public static final long REFRESH_TOKEN_VALIDITY_Days = 10; // 10일
    private final RefreshTokenRepository refreshTokenRepository;

    public String issueRefreshToken(AppUser appUser) {
        // 1. 랜덤 원본 생성
        String rawToken = TokenUtil.generateRawToken(); // <- 클라이언트에게 줄 값

        // 2. DB에 저장할 해시
        String tokenHash = TokenUtil.sha256Hex(rawToken);

        // 3. DB insert (JPA pseudo code)
        RefreshToken entity = RefreshToken.builder()
                .user(appUser)
                .tokenHash(tokenHash)
                .expiresAt(OffsetDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_Days))
                .build();

        refreshTokenRepository.save(entity);
        return rawToken;
    }


    public AppUser validateAndGetUser(String oldRefreshToken) {
        String tokenHash = TokenUtil.sha256Hex(oldRefreshToken);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        if (refreshToken.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("만료된 리프레시 토큰입니다.");
        }

        return refreshToken.getUser();
    }
}
