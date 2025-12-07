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
            30, // TODO: 임시값. AppUser에 userScore 필드 추가 후 appuser.getUserScore()로 교체 필요
            13, // TODO: 임시값. AppUser에 linksCount 필드 추가 후 appuser.getLinksCount()로 교체 필요
            meetingParticipationCount
        );
    }
}
