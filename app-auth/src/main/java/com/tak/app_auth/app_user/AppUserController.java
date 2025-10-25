package com.tak.app_auth.app_user;

import com.tak.app_auth.dto.CreateAppUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/signup")
    public AppUser createAppUser(@RequestBody CreateAppUserRequest request) {
        return  appUserService.createAppUser(request);
    }

    @PostMapping("/login")
    public String login() {
        return "login 성공";
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
