package com.tak.app_media.controller;

import com.tak.app_media.dto.PresignedUploadResponse;
import com.tak.common.api.ApiResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(("/media/"))
public class MediaController {
    private final String exampleMediaLink = "https://playground-chat-media.s3.ap-northeast-2.amazonaws.com/image_example.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAWTUM7QILPOCFSKVX%2F20251103%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Date=20251103T132411Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=d954808facde15f0f439290286f0863a58ef1bc8e6c45ab83db62816a8defec1";

    @PostMapping("/presign/upload")
    public ResponseEntity<?> generatePresignedUploadUrl() {
        // In a real implementation, you would generate a presigned URL using AWS SDK here.
        return ResponseEntity.ok().body(ApiResponseBody.ok(new PresignedUploadResponse(
                "example.com",
                "example-key",
                Map.of("example-header-key", "example-header-value")
        )));
    }
    @GetMapping("/{id}")
    public String getMediaById() {
        return exampleMediaLink;
    }
}
