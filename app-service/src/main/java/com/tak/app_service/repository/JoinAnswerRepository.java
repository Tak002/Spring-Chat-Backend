package com.tak.app_service.repository;

import com.tak.app_service.entity.JoinAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinAnswerRepository extends JpaRepository<JoinAnswer, Long> {
    List<JoinAnswer> findAllByMeetingIdIn(List<Long> hostMeetingIds);
}
