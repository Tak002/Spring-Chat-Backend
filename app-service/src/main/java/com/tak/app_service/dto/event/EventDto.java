package com.tak.app_service.dto.event;

import com.tak.app_service.entity.Event;

import java.time.Instant;

public record EventDto(
        Long id,
        Long ownerId,
        String title,
        String description,
        Instant startAt,
        Instant endAt,
        String place,
        Long thumbnailId
) {
    public static EventDto from(Event event) {
        return new EventDto(
                event.getId(),
                event.getOwnerId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartAt(),
                event.getEndAt(),
                event.getPlace(),
                event.getThumbnailId()
        );
    }
}
