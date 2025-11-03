package com.tak.app_auth.appUser;

import com.tak.app_auth.dto.LoginResponse;
import com.tak.app_auth.dto.SignupRequest;
import com.tak.app_auth.dto.LoginRequest;
import com.tak.app_auth.dto.TokenDto;
import com.tak.common.api.ApiResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/")
    public String home() {
        return "signup";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/login-test")
    public String loginTestPage() {
        return "login-test";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> createAppUser(@RequestBody SignupRequest request) {
        try{
            AppUser appuser  = appUserService.createAppUser(request);
            return ResponseEntity.ok().body(appuser);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponseBody.fail("Signup Failed",e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try{
            // 로그인 성공 시, 토큰 2개 발급 (Access + Refresh)
            Map<String, Object> tokens = appUserService.login(request);
            // Refresh Token은 HttpOnly 쿠키로 내려주는 것도 가능(웹의 경우)
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, (String) tokens.get("refreshCookie"))
                    .body(ApiResponseBody.ok(new LoginResponse(
                            (String) tokens.get("accessToken"),
                            LoginResponse.AuthUser.fromAppUser((AppUser) tokens.get("appUser"))
                    )));

        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseBody.fail("Login Failed",e.getMessage()));
        }
    }

    @PostMapping("/login-test")
    @ResponseBody
    public String loginTest(@RequestBody TokenDto token) {
        try{
            return appUserService.loginTest(token.token());
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken) {
        String logoutRefreshCookie = appUserService.logout(refreshToken);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, logoutRefreshCookie)
                .build();
    }

    @PostMapping("/rotate-refresh-token")
    public ResponseEntity<?> rotateRefreshToken(@CookieValue String refreshToken) {
        try {
            Map<String, Object> tokens = appUserService.rotateRefreshToken(refreshToken);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, (String) tokens.get("refreshCookie"))
                    .body(Map.of("accessToken", tokens.get("accessToken")));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
//    토큰 동작 테스트용 엔드포인트
    @GetMapping("/token-test")
    public ResponseEntity<Map<String, String>> tokenTest(@RequestHeader String accessToken, @CookieValue String refresh_token) {
        Map<String, String> tokenData = appUserService.tokenTest(accessToken, refresh_token);
        return ResponseEntity.ok(tokenData);

    }
}
