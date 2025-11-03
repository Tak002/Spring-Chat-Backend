package com.tak.app_media.dto;

import java.util.Map;

public record PresignedUploadResponse(String uploadUrl,
                                      String key,
                                      Map<String, String> headers) {
}
