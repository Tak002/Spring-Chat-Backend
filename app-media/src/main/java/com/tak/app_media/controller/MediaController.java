package com.tak.app_media.controller;

import com.tak.app_media.dto.PresignedUrlResponse;
import com.tak.app_media.service.MediaService;
import com.tak.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/media/"))
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    // input: 어디에 쓸 이미지인지, 선택적으로 userId
    @PostMapping("/upload/{type}")
    public ApiResponse<?> generatePresignedUploadUrl(@PathVariable String type, @RequestAttribute("userId") Long userId) {
        PresignedUrlResponse presignedUrlResponse = mediaService.generatePresignedURL(type, userId);
        return ApiResponse.ok(presignedUrlResponse);
    }
    @GetMapping("/{mediaId}")
    public ApiResponse<?> getMediaById(@PathVariable Long mediaId) {
        try{
            String mediaUrl = mediaService.getMediaUrl(mediaId);
            return ApiResponse.ok(mediaUrl);
        }
        catch (Exception e){
            return ApiResponse.fail("MEDIA.NOT_FOUND", "존재하지 않는 mediaId");
        }
    }
}
