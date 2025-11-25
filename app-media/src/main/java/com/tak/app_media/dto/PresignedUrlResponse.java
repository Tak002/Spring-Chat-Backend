package com.tak.app_media.dto;

public record PresignedUrlResponse(
        Long mediaId,
        String uploadUrl
) {
}
