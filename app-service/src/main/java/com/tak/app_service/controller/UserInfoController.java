package com.tak.app_service.controller;

import com.tak.app_service.dto.user.UserInfo;
import com.tak.app_service.service.UserInfoService;
import com.tak.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;

    @GetMapping("/me")
    public ApiResponse<?> getMyProfile(@RequestAttribute("userId") Long userId) {
        UserInfo userInfo = userInfoService.getUserProfile(userId);
        return ApiResponse.ok(userInfo);
    }

    //todo 유저 프로필 수정 기능 구현
    @PostMapping("/me")
    public ApiResponse<?> updateMyProfile(@RequestAttribute("userId") Long userId){
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getUserProfile(@PathVariable Long id) {
        UserInfo userInfo = userInfoService.getUserProfile(id);
        return ApiResponse.ok(userInfo);
    }
}
