package com.tak.app_service.dto.forms;

import java.util.List;

public record FormAnswerRequest(
        List<List<Object>> answers
) {

    public void validate() {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException("응답 목록이 비어 있습니다.");
        }

        for (List<Object> item : answers) {
            if (item == null || item.size() != 2) {
                throw new IllegalArgumentException("각 항목은 [번호, 값] 2개 요소여야 합니다.");
            }

            Object number = item.get(0);
            Object value = item.get(1);

            if (!(number instanceof Number)) {
                throw new IllegalArgumentException("첫 번째 요소는 질문 번호(정수)여야 합니다.");
            }
            if (!(value instanceof String)) {
                throw new IllegalArgumentException("두 번째 요소는 응답 값(문자열)이어야 합니다.");
            }

            int num = ((Number) number).intValue();
            String text = ((String) value).trim();

            if (num <= 0) {
                throw new IllegalArgumentException("질문 번호는 1 이상의 정수여야 합니다.");
            }
            if (text.isEmpty()) {
                throw new IllegalArgumentException("응답 값은 비어 있을 수 없습니다.");
            }
        }
    }

    /**
     * DB answers_json 컬럼에 저장할 JSON 문자열 생성
     * 결과 예시:
     * {"answers":[[1,"값1"],[2,"값2"]]}
     */
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"answers\":[");

        for (int i = 0; i < answers.size(); i++) {
            List<Object> item = answers.get(i);

            int number = ((Number) item.get(0)).intValue();
            String value = item.get(1).toString()
                    .replace("\\", "\\\\")   // 역슬래시 이스케이프
                    .replace("\"", "\\\"");  // 큰따옴표 이스케이프

            sb.append("[");
            sb.append(number).append(",");
            sb.append("\"").append(value).append("\"");
            sb.append("]");

            if (i < answers.size() - 1) {
                sb.append(",");
            }
        }

        sb.append("]}");
        return sb.toString();
    }
}
