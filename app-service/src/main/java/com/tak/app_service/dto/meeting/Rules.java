package com.tak.app_service.dto.meeting;

public record Rules(
        Gender gender,        // null이면 제한 없음
        AgeRange ageRange    // null이면 제한 없음
) {
    public enum Gender { ANY, MALE, FEMALE }

    public record AgeRange(
            Integer min,          // null 허용
            Integer max           // null 허용
    ) {}
}