package com.tak.app_service.dto.forms;

import java.util.List;

public record FormRequest(
        List<List<Object>> questions
) {
    public void validate() {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("질문 목록이 비어 있습니다.");
        }

        for (List<Object> item : questions) {
            if (item.size() != 2) {
                throw new IllegalArgumentException("각 항목은 [번호, 내용] 2개 요소여야 합니다.");
            }
            if (!(item.get(0) instanceof Number)) {
                throw new IllegalArgumentException("첫 번째 요소는 번호(정수)여야 합니다.");
            }
            if (!(item.get(1) instanceof String)) {
                throw new IllegalArgumentException("두 번째 요소는 질문 내용(문자열)이어야 합니다.");
            }

            int number = ((Number) item.get(0)).intValue();
            String text = (String) item.get(1);

            if (number <= 0) {
                throw new IllegalArgumentException("번호는 1 이상의 정수여야 합니다.");
            }
            if (text.isBlank()) {
                throw new IllegalArgumentException("질문 내용은 비어 있을 수 없습니다.");
            }
        }
    }
}
