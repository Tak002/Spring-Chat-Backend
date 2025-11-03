package com.tak.app_auth.appUser;

import com.tak.app_auth.dto.CreateAppUserRequest;
import com.tak.app_auth.dto.LoginRequest;
import com.tak.app_auth.refreshToken.RefreshToken;
import com.tak.app_auth.refreshToken.RefreshTokenService;
import com.tak.app_auth.util.PasswordHasher;
import com.tak.app_auth.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final RefreshTokenService refreshtokenService;
    //todo 이메일, 비밀번호 유효성 검사
    public AppUser createAppUser(CreateAppUserRequest request) {
        if(appUserRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        if(request.getPasswordRow() == null || request.getPasswordRow().isEmpty()){
            throw new IllegalArgumentException("비밀번호는 비어있을 수 없습니다." );
        }
        AppUser appUser = AppUser.builder()
            .email(request.getEmail())
            .passwordHash(PasswordHasher.hash(request.getPasswordRow()))
            .role(request.getRole() != null ? request.getRole() : AppUser.Role.user)
            .build();
        return appUserRepository.save(appUser);

    }

    public Map<String,String> login(LoginRequest request) {
        // Email 검증
        AppUser appUser = appUserRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다: " + request.getEmail()));

        // Password 검증
        if(!appUser.getPasswordHash().equals(PasswordHasher.hash(request.getPasswordRow()))){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return generateTokens(appUser);
    }



    public String loginTest(String token) {
        if(TokenUtil.validateAccessToken(token)){
            return TokenUtil.getUserIdFromAccessToken(token);
        }
        else{
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    public String logout(String refreshToken) {
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(0)
                .build();
        return refreshCookie.toString();
    }

    public Map<String,String> rotateRefreshToken(String oldRefreshToken) {
        AppUser appUser = refreshtokenService.validateAndGetUser(oldRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
        return generateTokens(appUser);
    }

    private Map<String, String> generateTokens(AppUser appUser) {
        String accessToken= TokenUtil.generateAccessToken(String.valueOf(appUser.getId()));
        String refreshTokenRow = refreshtokenService.issueRefreshToken(appUser);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshTokenRow)
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(14))
                .build();

        return Map.of("accessToken", accessToken, "refreshCookie", refreshCookie.toString());
    }

    // return String of Access token validity, id, expiration + refreshToken validity, refreshToken.id, user.id, user.email, expiration
    public Map<String,String> tokenTest(String accessToken, String rowRefreshToken) {
        String accessTokenUserId;

        String accessTokenExpiration;
        String accessTokenValidity = TokenUtil.validateAccessToken(accessToken)? "Valid" : "Invalid";
        if(accessTokenValidity.equals("Valid")){
            accessTokenUserId = TokenUtil.getUserIdFromAccessToken(accessToken);
            accessTokenExpiration = String.valueOf(TokenUtil.getExpirationDateFromAccessToken(accessToken));
        }else {
            accessTokenUserId = null;
            accessTokenExpiration = null;
        }

        String refreshTokenValidity;
        String refreshTokenUserId;
        String refreshTokenExpiration;
        Optional<RefreshToken> refreshToken = refreshtokenService.validateAndGetRefreshToken(rowRefreshToken);
        if (refreshToken.isPresent()) {
            refreshTokenValidity = "valid";
            refreshTokenUserId = String.valueOf(refreshToken.get().getUser().getId());
            refreshTokenExpiration = String.valueOf(refreshToken.get().getExpiresAt());
        } else {
            refreshTokenValidity = "invalid";
            refreshTokenUserId = null;
            refreshTokenExpiration = null;
        }

        return Map.of(
                "accessTokenValidity", accessTokenValidity,
                "accessTokenUserId", accessTokenUserId,
                "accessTokenExpiration", accessTokenExpiration,
                "refreshTokenValidity", refreshTokenValidity,
                "refreshTokenUserId", refreshTokenUserId,
                "refreshTokenExpiration", refreshTokenExpiration
        );



    }
}
