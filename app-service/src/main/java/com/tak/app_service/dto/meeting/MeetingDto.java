package com.tak.app_service.dto.meeting;



import com.tak.app_service.entity.Meeting;
import com.tak.app_service.entity.enums.MeetingStatus;

import java.time.Instant;
import java.time.LocalDateTime;

public record MeetingDto(
        Long id,

        Long hostId,
        String hostNickname,

        String title,
        String description,

        LocalDateTime startAt,
        LocalDateTime endAt,

        String place,
        Integer maxMembers,

        String thumbnailId,
        Long linkedEventId,

        MeetingStatus status,

        Rules.Gender gender,
        Integer minAge,
        Integer maxAge,

        Instant createdAt,
        Instant updatedAt
) {
    public static MeetingDto toDto(Meeting m) {
        return new MeetingDto(
                m.getId(),
                m.getHost().getId(),
                m.getHost().getNickname(),
                m.getTitle(),
                m.getDescription(),
                m.getStartAt(),
                m.getEndAt(),
                m.getPlace(),
                m.getMaxMembers(),
                m.getThumbnail() != null ? m.getThumbnail().getId() : null,
                m.getLinkedEvent() != null ? m.getLinkedEvent().getId() : null,
                m.getStatus(),
                m.getGender(),
                m.getMinAge(),
                m.getMaxAge(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }

}
