package com.tak.app_service.user;

import com.tak.app_service.dto.CurrentUserDto;
import com.tak.common.api.ApiResponseBody;
import com.tak.common.appUser.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/users")
public class UserController {

    //todo AccessToken 실제 유저 정보 연동
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        return ResponseEntity.ok().body(ApiResponseBody.ok(new CurrentUserDto("dummy-access",
                new CurrentUserDto.AuthUser("1", "1234", "nickname", AppUser.Role.user, AppUser.Status.active))));
    }

    //todo 유저 프로필 수정 기능 구현
    @PostMapping("/me")
    public ResponseEntity<?> updateMyProfile() {
        return ResponseEntity.ok().body(ApiResponseBody.ok());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@RequestParam Long id) {
        return ResponseEntity.ok().body(ApiResponseBody.ok(new CurrentUserDto("dummy-access",
                new CurrentUserDto.AuthUser(id.toString(), "1234", "nickname", AppUser.Role.user, AppUser.Status.active))));
    }
}
