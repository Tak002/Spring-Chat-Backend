package com.tak.app_service.dto;

import com.tak.common.appUser.AppUser;

public record CurrentUserDto(
        String accessToken,
        AuthUser user
) {
    public record AuthUser(
            String id, String email, String nickname, AppUser.Role role, AppUser.Status status // ACTIVE/DEACTIVATED/...
    ) {
        public static AuthUser fromAppUser(AppUser appUser) {
            return new AuthUser(
                    appUser.getId().toString(),
                    appUser.getEmail(),
                    appUser.getNickname(),
                    appUser.getRole(),
                    appUser.getStatus()
            );
        }
    }
}