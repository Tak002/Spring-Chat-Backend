package com.tak.app_service.controller;

import com.tak.app_service.dto.event.EventCreateRequest;
import com.tak.app_service.dto.event.EventDto;
import com.tak.app_service.entity.Event;
import com.tak.app_service.service.EventService;
import com.tak.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;


    // 이벤트 등록
    @PostMapping
    public ApiResponse<?> createEvent(@RequestBody EventCreateRequest eventCreateRequest, @RequestAttribute("userId") Long userId) {
        Event event = eventService.createEvent(eventCreateRequest, userId);
        EventDto eventDto = EventDto.from(event);
        return ApiResponse.ok(eventDto);
    }

    // 이벤트 조회
    @GetMapping("/{id}")
    public ApiResponse<?> getEvent(@PathVariable String id) {
        try{
            Long idl = Long.parseLong(id);
            Event event = eventService.getEvent(idl);
            if(event == null) {
                return ApiResponse.fail("Not Found","Event not found");
            }
            EventDto eventDto = EventDto.from(event);
            return ApiResponse.ok(eventDto);
        }catch (Exception e){
            return ApiResponse.fail("Wrong Path","Invalid event ID");
        }
    }

    // 전체 이벤트 조회
    @GetMapping
    public ApiResponse<?> listEvents() {
        List<Event> events = eventService.listEvents();
        List<EventDto> eventDtos = events.stream().map(EventDto::from).toList();
        return ApiResponse.ok(eventDtos);
    }

    @GetMapping("/me")
    public ApiResponse<?> getMyEvents(@RequestAttribute("userId") Long userId) {
        List<Event> events = eventService.myEvents(userId);
        if(events.isEmpty()) {
            return ApiResponse.ok(List.of());
        }
        List<EventDto> eventDtos = events.stream().map(EventDto::from).toList();
        return ApiResponse.ok(eventDtos);    }
}
