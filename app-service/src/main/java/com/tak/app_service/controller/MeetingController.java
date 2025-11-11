package com.tak.app_service.controller;

import com.tak.app_service.dto.meeting.MeetingCreateRequest;
import com.tak.app_service.dto.meeting.MeetingDto;
import com.tak.app_service.dto.meeting.Rules;
import com.tak.common.api.ApiResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/meetings")
public class MeetingController {
    MeetingDto dumy_meeting = new MeetingDto(
            "meeting-1234",
            "Sample Meeting",
            "This is a sample meeting description.",
            LocalDateTime.of(2025, 11, 7, 10, 0, 0),
            LocalDateTime.of(2025, 11, 7, 12, 0, 0),
            "Sample Place",
            10,
            new Rules(
                Rules.Gender.FEMALE,
                new Rules.AgeRange(18, 25),
                new Rules.Affiliation(Rules.Scope.COLLEGE, List.of("Engineering", "Business"))
            ),
            List.of("tag1", "tag2"),
            "joinform-5678",
            "thumbnail-5678"
    );
    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody MeetingCreateRequest meetingCreateRequest) {
        //todo 모임 생성 로직 구현
        return ResponseEntity.ok().body(ApiResponseBody.ok(new MeetingDto(
                "dummy-meeting-id",
                meetingCreateRequest.title(),
                meetingCreateRequest.description(),
                meetingCreateRequest.startAt(),
                meetingCreateRequest.endAt(),
                meetingCreateRequest.place(),
                meetingCreateRequest.participantLimit(),
                new Rules(
                        meetingCreateRequest.rules().gender(),
                        meetingCreateRequest.rules().ageRange(),
                        meetingCreateRequest.rules().affiliation()
                ),
                meetingCreateRequest.tags(),
                meetingCreateRequest.joinFormId(),
                meetingCreateRequest.thumbnailId()
        )));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMeeting() {
        //todo 모임 조회 로직 구현
        return ResponseEntity.ok().body(ApiResponseBody.ok(dumy_meeting));
    }
}
