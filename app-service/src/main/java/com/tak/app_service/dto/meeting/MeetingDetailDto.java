package com.tak.app_service.dto.meeting;

import com.tak.app_service.entity.Meeting;
import com.tak.app_service.entity.enums.MeetingStatus;
import com.tak.common.appUser.AppUser;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record MeetingDetailDto (
        Long id,

        Long hostId,

        String title,
        String description,

        LocalDateTime startAt,
        LocalDateTime endAt,

        String place,
        Integer maxMembers,

        Long thumbnailId,
        Long linkedEventId,

        MeetingStatus status,

        Rules.Gender gender,
        Integer minAge,
        Integer maxAge,

        Long joinFormId,

        Instant createdAt,
        Instant updatedAt,
        List<AppUser> members
){
    public static MeetingDetailDto from(Meeting meeting, List<AppUser> members) {
        return new MeetingDetailDto(
                meeting.getId(),
                meeting.getHostId(),
                meeting.getTitle(),
                meeting.getDescription(),
                meeting.getStartAt(),
                meeting.getEndAt(),
                meeting.getPlace(),
                meeting.getMaxMembers(),
                meeting.getThumbnailId(),
                meeting.getLinkedEventId(),
                meeting.getStatus(),
                meeting.getGender(),
                meeting.getMinAge(),
                meeting.getMaxAge(),
                meeting.getJoinFormId(),
                meeting.getCreatedAt(),
                meeting.getUpdatedAt(),
                members
        );
    }
}
