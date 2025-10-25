package com.tak.app_auth.app_user;

import com.tak.app_auth.dto.CreateAppUserRequest;
import com.tak.app_auth.dto.LoginAppUserRequest;
import com.tak.app_auth.util.PasswordHasher;
import com.tak.app_auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
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

    public String login(LoginAppUserRequest request) {
        // Email 검증
        AppUser appUser = appUserRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다: " + request.getEmail()));

        // Password 검증
        if(!appUser.getPasswordHash().equals(PasswordHasher.hash(request.getPasswordRow()))){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JwtToken 생성 (생략)
        return jwtUtil.generateToken(String.valueOf(appUser.getId()));
    }

    public String loginTest(String token) {
        if(jwtUtil.validateToken(token)){
            return jwtUtil.getUserIdFromToken(token);
        }
        else{
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}
