package com.tak.app_auth.app_user;

import com.tak.app_auth.dto.CreateAppUserRequest;
import com.tak.app_auth.dto.LoginAppUserRequest;
import com.tak.app_auth.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public String login(@RequestBody LoginAppUserRequest request) {
        try{
            return appUserService.login(request);
        }catch (Exception e){
            return e.getMessage();
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
