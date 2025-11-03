package com.tak.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        //todo code enum 으로 변경
        String code,              // e.g. "AUTH.CODE_INVALID"
        String message,
        Map<String, Object> details
) {}
