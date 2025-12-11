package com.tak.app_service.controller;

import com.tak.app_service.dto.forms.FormAnswerRequest;
import com.tak.app_service.dto.forms.FormAnswerResponse;
import com.tak.app_service.service.FormService;
import com.tak.app_service.service.MeetingService;
import com.tak.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/form-answers")
@RequiredArgsConstructor
public class FormAnswerController {
    private final FormService formService;
    private final MeetingService meetingService;

    // 폼 답변하기
    @PostMapping
    public ApiResponse<?> submitFormAnswer(@RequestAttribute("userId") Long userId, @RequestParam Long meetingId, @RequestBody FormAnswerRequest formAnswerRequest) {
        FormAnswerResponse formAnswerResponse;
        try {
            formAnswerRequest.validate();
        } catch (Exception e) {
            return ApiResponse.fail("Invalid Request", "Form answer request is invalid: " + e.getMessage());
        }
        try {
            formAnswerResponse = formService.submitFormAnswer(userId, meetingId, formAnswerRequest);
        } catch (Exception e) {
            return ApiResponse.fail("Submission Failed", "Failed to submit form answer: " + e.getMessage());
        }
        return ApiResponse.ok(formAnswerResponse);
    }

    // 내 미팅의 폼 답변 조회하기
    @GetMapping
    public ApiResponse<?> getFormAnswer(@RequestAttribute("userId") Long userId){
        return ApiResponse.ok(formService.getFormAnswersByHostId(userId));
    }
    // 유저 승인하기

}
