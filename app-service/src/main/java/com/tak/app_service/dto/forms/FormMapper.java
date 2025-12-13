package com.tak.app_service.dto.forms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tak.app_service.entity.JoinAnswer;
import com.tak.app_service.entity.JoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FormMapper {

    private final ObjectMapper objectMapper;

    // === 기존: JoinForm -> FormResponse ===
    public FormResponse toFormResponse(JoinForm joinForm) {
        List<List<Object>> questions = extractNestedArray(joinForm.getQuestionsJson(), "questions");

        return new FormResponse(
                joinForm.getId(),
                questions,
                joinForm.getCreatedAt()
        );
    }

    // === 새로 추가: JoinAnswer -> AnswerResponse ===
    public FormAnswerResponse toAnswerResponse(JoinAnswer joinAnswer,Long formId) {
        List<List<Object>> answers = extractNestedArray(joinAnswer.getAnswersJson(), "answers");

        return new FormAnswerResponse(
                joinAnswer.getMeetingId(),
                formId,
                joinAnswer.getUserId(),
                answers,
                joinAnswer.getAnsweredAt()
        );
    }

    // === 공통 JSON 파싱 로직 ===
    private List<List<Object>> extractNestedArray(String json, String key) {
        try {
            Map<String, List<List<Object>>> map =
                    objectMapper.readValue(json,
                            new TypeReference<Map<String, List<List<Object>>>>() {});
            return map.getOrDefault(key, List.of());
        } catch (Exception e) {
            throw new IllegalStateException("JSON 파싱 실패: key=" + key, e);
        }
    }
}
