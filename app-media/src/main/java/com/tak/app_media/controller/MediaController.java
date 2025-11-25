package com.tak.app_media.controller;

import com.tak.app_media.dto.PresignedUploadResponse;
import com.tak.app_media.dto.PresignedUrlResponse;
import com.tak.common.api.ApiResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(("/media/"))
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    // input: 어디에 쓸 이미지인지, 선택적으로 userId
    @PostMapping("/upload/{type}")
    public ResponseEntity<?> generatePresignedUploadUrl(@PathVariable String type, @RequestAttribute("userId") Long userId) {
        PresignedUrlResponse presignedUrlResponse = mediaService.generatePresignedURL(type, userId);
        return ResponseEntity.ok().body(ApiResponseBody.ok(presignedUrlResponse));
    }
    @GetMapping("/{mediaId}")
    public String getMediaById(@PathVariable Long mediaId) {
        return mediaService.getMediaUrl(mediaId);
    }
}
