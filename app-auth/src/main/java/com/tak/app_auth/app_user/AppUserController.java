package com.tak.app_auth.app_user;

import com.tak.app_auth.dto.CreateAppUserRequest;
import com.tak.app_auth.dto.LoginRequest;
import com.tak.app_auth.dto.LoginResponse;
import com.tak.app_auth.dto.TokenDto;
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

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }
    @PostMapping("/signup")
    @ResponseBody
    public String createAppUser(@RequestBody CreateAppUserRequest request) {
        try{
            return appUserService.createAppUser(request).toString();
        }catch (Exception e){
            return e.getMessage();
        }
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try{
            // 로그인 성공 시, 토큰 2개 발급 (Access + Refresh)
            Map<String, String> tokens = appUserService.login(request);

            // Access Token은 클라이언트가 직접 저장
            // Refresh Token은 HttpOnly 쿠키로 내려주는 것도 가능(웹의 경우)
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, tokens.get("refreshCookie"))
                    .body(Map.of("accessToken", tokens.get("accessToken")));

        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/login-test")
    public String loginTestPage() {
        return "login-test";
    }
    @PostMapping("/login-test")
    @ResponseBody
    public String loginTest(@RequestBody TokenDto token) {
        try{
            return appUserService.loginTest(token.getToken());
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @PostMapping("/logout")
    public String logout() {
        return "login 성공";
    }

    @PostMapping("/verify-email")
    public String verifyEmail() {
        return "verify-email 성공";
    }
}
