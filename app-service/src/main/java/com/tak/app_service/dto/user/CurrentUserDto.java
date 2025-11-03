package com.tak.app_service.dto.user;

import com.tak.common.appUser.AppUser;

import java.time.LocalDate;

public record CurrentUserDto(
        String accessToken,
        AuthUser user
) {
    public record AuthUser(
            String id,
            String email,
            String nickname,
            AppUser.Role role,
            AppUser.Status status, // ACTIVE/DEACTIVATED/...
            String department,
            String bio,
            LocalDate birthday
    ) {
        public static AuthUser fromAppUser(AppUser appUser) {
            return new AuthUser(
                    appUser.getId().toString(),
                    appUser.getEmail(),
                    appUser.getNickname(),
                    appUser.getRole(),
                    appUser.getStatus()
                    , appUser.getDepartment()
                    , appUser.getBio()
                    , appUser.getBirthDate()
            );
        }
    }
}