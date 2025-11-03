package com.tak.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiError {
    private final String code;              // 예: "AUTH.CODE_INVALID", "MEETING.FULL"
    private final String message;           // 사용자/개발자 공용 기본 메시지
    private final Map<String, Object> details; // 필드 오류 등 세부정보(선택)

    public ApiError(String code, String message, Map<String, Object> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public Map<String, Object> getDetails() { return details; }
}
