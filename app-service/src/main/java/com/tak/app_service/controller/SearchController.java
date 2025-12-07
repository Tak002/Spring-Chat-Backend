package com.tak.app_service.controller;

import com.tak.app_service.dto.event.EventDto;
import com.tak.app_service.dto.meeting.MeetingDto;
import com.tak.app_service.dto.search.SearchResponse;
import com.tak.app_service.service.EventService;
import com.tak.app_service.service.MeetingService;
import com.tak.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final MeetingService meetingService;
    private final EventService eventService;

    // 통합 검색 API
    // target: event, meeting, null(전체)
    @GetMapping("/api/search")
    public ApiResponse<?> search(@RequestParam String keyword, @RequestParam(required = false) String target) {
        List<EventDto> events = null;
        List<MeetingDto> meetings = null;

        if(target != null && !target.equals("event") && !target.equals("meeting")) {
            return ApiResponse.fail("INVALID_TARGET", "target은 'event', 'meeting' 또는 null이어야 합니다");
        }
        if(keyword.isEmpty()) {
            return ApiResponse.fail("Invalid Request", "Keyword cannot be empty");
        }

        if(target == null || target.equals("event")) {
            // 이벤트 검색
            events= eventService.getEventsByTitleKeyword(keyword); // 이벤트 검색 로직 추가
        }
        if(target == null || target.equals("meeting")) {
            // 미팅 검색
            meetings= meetingService.getMeetingsByTitleKeyword(keyword); // 미팅 검색 로직 추가
        }

        return ApiResponse.ok(new SearchResponse(events, meetings));
    }
}
