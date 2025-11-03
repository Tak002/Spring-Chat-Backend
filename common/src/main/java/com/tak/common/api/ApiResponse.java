package com.tak.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiResponse<T> {
    // ---- getters ----
    private final boolean success;
    private final T data;
    private final ApiError error;
    private final Map<String, Object> meta;
    private final String traceId;

    private ApiResponse(boolean success, T data, ApiError error,
                        Map<String, Object> meta, String traceId) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.meta = meta;
        this.traceId = traceId;
    }

    // ---- 성공 응답 ----
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, null, null);
    }

    public static <T> ApiResponse<T> ok(T data, Map<String, Object> meta) {
        return new ApiResponse<>(true, data, null,
                meta != null ? meta : Collections.emptyMap(), null);
    }

    // ---- 실패 응답 ----
    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(false, null, new ApiError(code, message, null), null, null);
    }

    public static <T> ApiResponse<T> fail(String code, String message, Map<String, Object> details) {
        return new ApiResponse<>(false, null, new ApiError(code, message, details), null, null);
    }

    // 선택: traceId 주입 편의
    public ApiResponse<T> withTraceId(String traceId) {
        return new ApiResponse<>(this.success, this.data, this.error, this.meta, traceId);
    }

}
