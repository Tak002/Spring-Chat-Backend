package com.tak.app_service.dto.event;

import java.time.LocalDateTime;
import java.util.List;

public record EventCreateRequest(
        String title,
        String description,
        LocalDateTime startAt,   // e.g. "2025-11-07T10:00:00"
        LocalDateTime endAt,     // e.g. "2025-11-07T12:00:00"
        String place,
        List<String> tags,
        String thumbnailId
) {}