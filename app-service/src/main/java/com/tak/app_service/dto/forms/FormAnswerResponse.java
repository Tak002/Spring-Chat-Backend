package com.tak.app_service.dto.forms;

import java.time.Instant;
import java.util.List;

public record FormAnswerResponse(
        Long meetingId,
        Long fromId,
        Long userId,
        List<List<Object>> answers,
        Instant answeredAt
) {}
