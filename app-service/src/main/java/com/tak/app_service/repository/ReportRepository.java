package com.tak.app_service.repository;

import com.tak.app_service.entity.Report;
import com.tak.app_service.entity.enums.ReportStatus;
import com.tak.app_service.entity.enums.ReportTargetType;
import com.tak.common.appUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByStatus(ReportStatus status);

    Optional<Report> findByReporterAndTargetTypeAndTargetIdAndStatus(
            AppUser reporter,
            ReportTargetType targetType,
            String targetId,
            ReportStatus status
    );
}
