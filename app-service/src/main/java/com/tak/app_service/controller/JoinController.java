package com.tak.app_service.controller;

import com.tak.app_service.dto.forms.FormAnswerRequest;
import com.tak.app_service.dto.forms.FormAnswerResponse;
import com.tak.app_service.repository.MeetingMemberRepository;
import com.tak.app_service.service.FormService;
import com.tak.app_service.service.MeetingService;
import com.tak.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/join")
@RequiredArgsConstructor
@Slf4j
public class JoinController {
    private final FormService formService;
    private final MeetingService meetingService;
    private final MeetingMemberRepository meetingMemberRepository;

    // 미팅 참가하기
    // 폼이 필요없으면 바로 승인, 필요 있다면 폼 답변 제출 후 승인
    @PostMapping
    public ApiResponse<?> submitFormAnswer(@RequestAttribute("userId") Long userId, @RequestParam Long meetingId, @RequestBody(required = false) FormAnswerRequest formAnswerRequest) {
        boolean needFormAnswer = meetingService.isNeedFormAnswer(meetingId);

        FormAnswerResponse formAnswerResponse;
        if(meetingMemberRepository.findByUserIdAndMeetingId(userId,meetingId).isPresent()) {
            return ApiResponse.fail("Already Joined", "User has already joined the meeting.");
        }
        if(needFormAnswer && formAnswerRequest == null) {
            return ApiResponse.fail("Form Answer Required", "This meeting requires a form answer for joining.");
        }
        if(needFormAnswer) {
            try {
                formAnswerRequest.validate();
            } catch (Exception e) {
                log.info("Form answer validation failed: " + e.getMessage());
                return ApiResponse.fail("Invalid Request", "Form answer request is invalid: " + e.getMessage());
            }
            try {
                formAnswerResponse = formService.submitFormAnswer(userId, meetingId, formAnswerRequest);
            } catch (Exception e) {
                log.info("Form answer submitting failed: " + e.getMessage());
                return ApiResponse.fail("Submission Failed", "Failed to submit form answer: " + e.getMessage());
            }

            return ApiResponse.ok(formAnswerResponse);
        }
        return ApiResponse.ok(meetingService.addUserToMeeting(meetingId, userId));
    }

    @PostMapping("/approve")
    public ApiResponse<?> approveFormAnswer(@RequestAttribute("userId") Long hostId, @RequestParam Long answerId) {
        try {
            meetingService.approveAnswer(hostId, answerId);
            return ApiResponse.ok("Form answer approved and user added to the meeting.");
        } catch (Exception e) {
            return ApiResponse.fail("Approval Failed", "Failed to approve form answer: " + e.getMessage());
        }
    }
    // 내 미팅의 폼 답변 조회하기
    @GetMapping
    public ApiResponse<?> getFormAnswer(@RequestAttribute("userId") Long userId){
        return ApiResponse.ok(formService.getFormAnswersByHostId(userId));
    }

}
