package com.tak.app_auth.appUser;

import com.tak.app_auth.dto.CreateAppUserRequest;
import com.tak.app_auth.dto.LoginRequest;
import com.tak.app_auth.refreshToken.RefreshTokenService;
import com.tak.app_auth.util.PasswordHasher;
import com.tak.app_auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final RefreshTokenService refreshtokenService;
    private final JwtUtil jwtUtil;
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
        if(jwtUtil.validateToken(token)){
            return jwtUtil.getUserIdFromToken(token);
        }
        else{
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    public String logout(String refreshToken) {
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(0)
                .build();
        return refreshCookie.toString();
    }

    public Map<String,String> rotateRefreshToken(String oldRefreshToken) {
        // 기존 리프레시 토큰 검증 및 사용자 조회
        try {
            AppUser appUser = refreshtokenService.validateAndGetUser(oldRefreshToken);
            return generateTokens(appUser);
        }catch (Exception e){
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }
    }

    private Map<String, String> generateTokens(AppUser appUser) {
        String accessToken= jwtUtil.generateToken(String.valueOf(appUser.getId()));
        String refreshTokenRow = refreshtokenService.issueRefreshToken(appUser);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshTokenRow)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(14))
                .build();

        return Map.of("accessToken", accessToken, "refreshCookie", refreshCookie.toString());
    }
}
