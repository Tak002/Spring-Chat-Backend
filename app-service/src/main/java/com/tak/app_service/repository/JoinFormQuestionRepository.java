package com.tak.app_service.repository;

import com.tak.app_service.entity.JoinFormQuestion;
import com.tak.app_service.entity.JoinForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinFormQuestionRepository extends JpaRepository<JoinFormQuestion, Long> {

    List<JoinFormQuestion> findByFormOrderByOrderNoAsc(JoinForm form);
}
