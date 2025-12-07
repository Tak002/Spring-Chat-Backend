package com.tak.app_service.dto.user;

import com.tak.common.appUser.AppUser;

public record UserInfo(
    Long userId,
    String nickname,
    String department,
    String bio,
    Long profileImageId,
    AppUser.Sex sex,
    Integer userScore, //0~100
    Integer linksCount,
    Integer meetingParticipationCount
) {
    public static UserInfo from(AppUser appuser,Integer meetingParticipationCount){
        return new UserInfo(
            appuser.getId(),
            appuser.getNickname(),
            appuser.getDepartment(),
            appuser.getBio(),
            appuser.getProfileImageId(),
            appuser.getSex(),
            30, // appuser.getUserScore()
            13, // appuser.getLinksCount()
            meetingParticipationCount
        );
    }
}
