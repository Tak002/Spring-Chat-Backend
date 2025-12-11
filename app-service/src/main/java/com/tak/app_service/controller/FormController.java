package com.tak.app_service.controller;

import com.tak.app_service.dto.forms.FormRequest;
import com.tak.app_service.service.FormService;
import com.tak.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("/api/forms")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;
    // 폼 등록하기
    @PostMapping
    public ApiResponse<?> createForm(@RequestAttribute("userId") Long userId, @RequestBody FormRequest formRequest) {
        formRequest.validate();
        return ApiResponse.ok();
    }
    // 폼 조회하기


}
