package com.tak.app_service.event;

import com.tak.app_service.dto.event.EventCreateRequest;
import com.tak.app_service.dto.event.EventDto;
import com.tak.common.api.ApiResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/events")
public class EventController {
    private final EventDto sampleEvent = new EventDto(
            "event-1234",
            "Sample Event",
            "This is a sample event description.",
            LocalDateTime.of(2025, 11, 7, 10, 0, 0),
            LocalDateTime.of(2025, 11, 7, 12, 0, 0),
            "Sample Place",
            List.of("tag1", "tag2"),
            "thumbnail-5678"
    );

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest eventCreateRequest) {
        // todo 이벤트 생성 로직 구현
        return ResponseEntity.ok().body(ApiResponseBody.ok(new EventDto(
                "dummy-event-id",
                eventCreateRequest.title(),
                eventCreateRequest.description(),
                eventCreateRequest.startAt(),
                eventCreateRequest.endAt(),
                eventCreateRequest.place(),
                eventCreateRequest.tags(),
                eventCreateRequest.thumbnailId()
        )));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable String id) {
        // todo 이벤트 조회 로직 구현
        return ResponseEntity.ok().body(ApiResponseBody.ok());
    }

    @GetMapping
    public ResponseEntity<?> listEvents() {
        // todo 이벤트 생성 로직 구현
        return ResponseEntity.ok().body(ApiResponseBody.ok(List.of(sampleEvent,sampleEvent,sampleEvent)));
    }
}
