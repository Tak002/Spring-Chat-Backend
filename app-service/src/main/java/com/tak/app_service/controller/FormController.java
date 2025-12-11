package com.tak.app_service.controller;

import com.tak.app_service.dto.forms.FormRequest;
import com.tak.app_service.service.FormService;
import com.tak.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;
    // 폼 등록하기
    @PostMapping
    public ApiResponse<?> createForm(@RequestAttribute("userId") Long userId, @RequestBody FormRequest formRequest) {
        try{
            formRequest.validate();
        }catch (Exception e){
            return ApiResponse.fail("Invalid Request","Form request is invalid: " + e.getMessage());
        }

        return ApiResponse.ok(formService.createForm(userId, formRequest));
    }

    @GetMapping
    public ApiResponse<?> getMyForms(@RequestAttribute("userId") Long userId){
        return ApiResponse.ok(formService.getForms(userId));
    }



}
