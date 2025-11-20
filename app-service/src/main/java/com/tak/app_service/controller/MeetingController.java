package com.tak.app_service.controller;

import com.tak.app_service.dto.meeting.MeetingCreateRequest;
import com.tak.app_service.dto.meeting.MeetingDto;
import com.tak.app_service.service.MeetingService;
import com.tak.common.api.ApiResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @GetMapping
    public ResponseEntity<?> getAllMeetings() {
        return ResponseEntity.ok().body(ApiResponseBody.ok(meetingService.getMeetings()));
    }
    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody MeetingCreateRequest meetingCreateRequest, @RequestAttribute("userId") Long userId) {
        //todo 모임 생성 로직 구현
        MeetingDto meeting = meetingService.createMeeting(meetingCreateRequest, userId);
        return ResponseEntity.ok().body(ApiResponseBody.ok(meeting));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMeeting(@PathVariable Long id) {
        //todo 모임 조회 로직 구현
        return ResponseEntity.ok().body(ApiResponseBody.ok(meetingService.getMeeting(id)));
    }
}
