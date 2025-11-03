package com.tak.app_service.dto.meeting;

import java.util.List;

public record Rules(
        Gender gender,        // null이면 제한 없음
        AgeRange ageRange,    // null이면 제한 없음
        Affiliation affiliation // null이면 제한 없음
) {
    public enum Gender { ANY, MALE, FEMALE }
    public enum Scope { NONE, COLLEGE, DEPARTMENT }

    public record AgeRange(
            Integer min,          // null 허용
            Integer max           // null 허용
    ) {}

    public record Affiliation(
            Scope match,               // null 또는 NONE 이면 조건X
            List<String> values    // 허용하는 학과/ 단과대의 리스트.
    ) {}
}