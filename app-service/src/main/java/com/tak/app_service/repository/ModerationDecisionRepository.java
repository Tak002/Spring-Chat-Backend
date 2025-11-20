package com.tak.app_service.repository;

import com.tak.app_service.entity.ModerationDecision;
import com.tak.app_service.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModerationDecisionRepository extends JpaRepository<ModerationDecision, Long> {

    List<ModerationDecision> findByReport(Report report);
}
