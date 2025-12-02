package com.tak.app_auth.email;

import com.tak.common.api.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/email/")
public class EmailAuthController {
    @PostMapping("/verify")
    public ApiResponse<?> verifyEmail(@RequestBody String email) {
        //todo 이메일 인증 구현
        return ApiResponse.ok(Map.of("status", "VERIFIED"));
    }
}
