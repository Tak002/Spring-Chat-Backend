package com.tak.app_service.dto.forms;

import java.time.Instant;
import java.util.List;

public record FormResponse(
        Long id,
        List<List<Object>> questions,
        Instant createdAt
){
}
