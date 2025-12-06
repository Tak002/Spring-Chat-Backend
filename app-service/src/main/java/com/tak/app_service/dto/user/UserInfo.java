package com.tak.app_service.dto.user;

public record UserInfo(
    Long userId,
    String nickname,
    String department,
    String bio,
    Long profileImageId,
    String sex,
    Integer userScore, //0~100
    Integer linksCount,
    Integer meetingParticipationCount
) {
}
