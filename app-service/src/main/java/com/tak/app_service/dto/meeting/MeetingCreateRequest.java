package com.tak.app_service.dto.meeting;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingCreateRequest(
        String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String place,
        Integer participantLimit,
        Rules rules,              // null이면 제한 없음
        List<String> tags,
        String joinFormId,        // null 허용
        Long thumbnailId,
        Long linkedEventId
) {}