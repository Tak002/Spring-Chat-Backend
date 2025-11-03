package com.tak.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseBody<T>(
        boolean success,
        T data,
        ApiError error,
        Map<String, Object> meta,
        String traceId
) {
    public static <T> ApiResponseBody<T> ok() { return new ApiResponseBody<>(true, null, null, null, null); }
    public static <T> ApiResponseBody<T> ok(T data) { return new ApiResponseBody<>(true, data, null, null, null); }
    public static <T> ApiResponseBody<T> ok(T data, Map<String, Object> meta) { return new ApiResponseBody<>(true, data, null, meta, null); }
    public static <T> ApiResponseBody<T> fail(String code, String message) { return new ApiResponseBody<>(false, null, new ApiError(code, message, null), null, null); }
}
