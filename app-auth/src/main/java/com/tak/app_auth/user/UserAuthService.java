package com.tak.app_auth.user;

import com.tak.app_auth.dto.SignupRequest;
import com.tak.app_auth.dto.LoginRequest;
import com.tak.app_auth.refreshToken.RefreshToken;
import com.tak.app_auth.refreshToken.RefreshTokenService;
import com.tak.app_auth.repository.AppUserRepository;
import com.tak.common.util.PasswordHasher;
import com.tak.common.util.TokenUtil;
import com.tak.common.appUser.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final AppUserRepository appUserRepository;
    private final RefreshTokenService refreshtokenService;
    //todo 이메일, 비밀번호 유효성 검사
    public AppUser createAppUser(SignupRequest request) {
        //todo certificationNumber 검증

        if(appUserRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.email());
        }
        if(request.passwordRow() == null || request.passwordRow().isEmpty()){
            throw new IllegalArgumentException("비밀번호는 비어있을 수 없습니다." );
        }
        AppUser appUser = AppUser.builder()
            .email(request.email())
            .passwordHash(PasswordHasher.hash(request.passwordRow()))
            .role(request.role() != null ? request.role() : AppUser.Role.user)
            .nickname(request.nickname())
            .department(request.department())
            .bio(request.bio())
            .birthDate(request.birthDate())
            .build();
        return appUserRepository.save(appUser);

    }

    public Map<String,Object> login(LoginRequest request) {
        // Email 검증
        AppUser appUser = appUserRepository.findByEmail(request.email()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다: " + request.email()));

        // Password 검증
        if(!appUser.getPasswordHash().equals(PasswordHasher.hash(request.passwordRow()))){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return generateTokens(appUser);
    }



    public String loginTest(String token) {
        if(TokenUtil.validateAccessTokenAndGetID(token)!=null){
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

    public Map<String,Object> rotateRefreshToken(String oldRefreshToken) {
        AppUser appUser = refreshtokenService.validateAndGetUser(oldRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
        return generateTokens(appUser);
    }

    private Map<String, Object> generateTokens(AppUser appUser) {
        String accessToken= TokenUtil.generateAccessToken(String.valueOf(appUser.getId()));
        String refreshTokenRow = refreshtokenService.issueRefreshToken(appUser);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshTokenRow)
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(14))
                .build();

        return Map.of("accessToken", accessToken, "refreshCookie", refreshCookie.toString(),"appUser", appUser);
    }

    // return String of Access token validity, id, expiration + refreshToken validity, refreshToken.id, user.id, user.email, expiration
    public Map<String,String> tokenTest(String accessToken, String rowRefreshToken) {
        String accessTokenUserId;

        String accessTokenExpiration;
        String accessTokenValidity = TokenUtil.validateAccessTokenAndGetID(accessToken)!=null? "Valid" : "Invalid";
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
