package com.tak.app_auth.dto;

import com.tak.app_auth.appUser.AppUser;

public record LoginResponse(
        String accessToken,
        AuthUser user
) {
    public record AuthUser(
            String id, String email, String nickname, AppUser.Role role, String status // ACTIVE/DEACTIVATED/...
    ) {
        public static AuthUser fromAppUser(AppUser appUser) {
            return new AuthUser(
                    appUser.getId().toString(),
                    appUser.getEmail(),
                    appUser.getNickname(),
                    appUser.getRole(),
                    appUser.getStatus().name()
            );
        }
    }
}