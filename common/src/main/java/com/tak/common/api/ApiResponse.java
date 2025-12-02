package com.tak.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error,
        Map<String, Object> meta,
        String traceId
) {
    public static <T> ApiResponse<T> ok() { return new ApiResponse<>(true, null, null, null, null); }
    public static <T> ApiResponse<T> ok(T data) { return new ApiResponse<>(true, data, null, null, null); }
    public static <T> ApiResponse<T> ok(T data, Map<String, Object> meta) { return new ApiResponse<>(true, data, null, meta, null); }
    public static <T> ApiResponse<T> fail(String code, String message) { return new ApiResponse<>(false, null, new ApiError(code, message, null), null, null); }
}
