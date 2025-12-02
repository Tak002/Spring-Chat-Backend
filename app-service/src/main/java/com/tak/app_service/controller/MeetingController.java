package com.tak.app_service.controller;

import com.tak.app_service.dto.meeting.MeetingCreateRequest;
import com.tak.app_service.dto.meeting.MeetingDto;
import com.tak.app_service.service.MeetingService;
import com.tak.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @GetMapping
    public ApiResponse<?> getAllMeetings() {
        return ApiResponse.ok(meetingService.getMeetings());
    }
    @PostMapping
    public ApiResponse<?> createMeeting(@RequestBody MeetingCreateRequest meetingCreateRequest, @RequestAttribute("userId") Long userId) {
        //todo 모임 생성 로직 구현
        MeetingDto meeting = meetingService.createMeeting(meetingCreateRequest, userId);
        return ApiResponse.ok(meeting);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getMeeting(@PathVariable Long id) {
        //todo 모임 조회 로직 구현
        try{
            return ApiResponse.ok(meetingService.getMeeting(id));

        }catch (Exception e){
            return ApiResponse.fail("EntityNotFoundException", e.getMessage());
        }
    }
}
