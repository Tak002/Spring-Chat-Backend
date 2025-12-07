package com.tak.app_service.repository;

import com.tak.app_service.entity.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
    Integer countByUserId(Long userId);

    @Query("select m.userId from MeetingMember m where m.meetingId = :meetingId")
    List<Long> findUserIdByMeetingId(Long meetingId);
}
