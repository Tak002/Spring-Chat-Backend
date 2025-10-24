package com.tak.app_auth.app_user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    public AppUser createAppUser(CreateAppUserRequest request) {
        if(appUserRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        System.out.println("request = " + request);
        AppUser appUser = AppUser.builder()
            .email(request.getEmail())
            .passwordHash(request.getPasswordHash())
            .role(request.getRole() != null ? request.getRole() : AppUser.Role.user)
            .build();
        return appUserRepository.save(appUser);

    }
}
