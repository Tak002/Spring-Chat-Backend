package com.tak.app_service.repository;

import com.tak.app_service.entity.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
    Integer countByUserId(Long userId);
}
