package com.tak.app_service.dto.event;

import java.time.LocalDateTime;
import java.util.List;

public record EventDto(
        String id,
        String title,
        String description,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String place,
        List<String> tags,
        String thumbnailId
) {}