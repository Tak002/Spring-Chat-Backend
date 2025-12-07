package com.tak.app_service.dto.user;

public record UserInfoUpdate(
    String nickname,
    String department,
    String bio,
    Long profileImageId
) {
}
