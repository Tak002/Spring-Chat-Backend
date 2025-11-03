package com.tak.app_auth.refreshToken;

import com.tak.common.appUser.AppUser;
import com.tak.app_auth.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    public final long REFRESH_TOKEN_VALIDITY_Days = TokenUtil.REFRESH_TOKEN_VALIDITY_Days; // 10일
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


    public Optional<AppUser> validateAndGetUser(String oldRefreshToken) {
        return validateAndGetRefreshToken(oldRefreshToken)
                .map(RefreshToken::getUser);
    }

    public Optional<RefreshToken> validateAndGetRefreshToken(String refreshToken) {
        String tokenHash = TokenUtil.sha256Hex(refreshToken);
        return refreshTokenRepository.findByTokenHash(tokenHash)
                .filter(token -> token.getExpiresAt().isAfter(OffsetDateTime.now()));
    }


}
