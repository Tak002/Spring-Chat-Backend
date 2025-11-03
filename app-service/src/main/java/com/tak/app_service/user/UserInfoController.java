package com.tak.app_service.user;

import com.tak.app_service.dto.CurrentUserDto;
import com.tak.common.api.ApiResponseBody;
import com.tak.common.appUser.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/api/users")
public class UserInfoController {

    //todo AccessToken 실제 유저 정보 연동
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        return ResponseEntity.ok().body(ApiResponseBody.ok(new CurrentUserDto("dummy-access",
                new CurrentUserDto.AuthUser("1", "1234", "nickname", AppUser.Role.user, AppUser.Status.active, "CS", "Hello, I'm using TAK!", LocalDate.of(2002,11,20)))));
    }

    //todo 유저 프로필 수정 기능 구현
    @PostMapping("/me")
    public ResponseEntity<?> updateMyProfile() {
        return ResponseEntity.ok().body(ApiResponseBody.ok());
    }
    
    //todo 유저 조회 연동
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable String id) {
        return ResponseEntity.ok().body(ApiResponseBody.ok(new CurrentUserDto("dummy-access",
                new CurrentUserDto.AuthUser(id, "1234", "nickname", AppUser.Role.user, AppUser.Status.active, "CS", "Hello, I'm using TAK!", LocalDate.of(2002,11,20)))));    }
}
