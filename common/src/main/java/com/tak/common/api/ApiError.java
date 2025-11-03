package com.tak.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        String code,              // e.g. "AUTH.CODE_INVALID"
        String message,
        Map<String, Object> details
) {}
