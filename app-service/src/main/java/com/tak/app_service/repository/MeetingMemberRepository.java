package com.tak.app_service.repository;

import com.tak.app_service.entity.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
    @Query("""
        select count(m)
        from MeetingMember m
        where m.userId = :userId
          and m.state = com.tak.app_service.entity.enums.MeetingMemberState.APPROVED
        """)
    Integer countByUserId(Long userId);

    @Query("""
        select m.userId
        from MeetingMember m
        where m.meetingId = :meetingId
          and m.state = com.tak.app_service.entity.enums.MeetingMemberState.APPROVED
        """)
    List<Long> findUserIdByMeetingId(Long meetingId);

    @Query("""
        select m.meetingId
        from MeetingMember m
        where m.userId = :userId
          and m.state = com.tak.app_service.entity.enums.MeetingMemberState.APPROVED
        """)
    List<Long> findMeetingIdByUserId(Long userId);

    void deleteAllByMeetingId(Long id);

    MeetingMember findByUserIdAndMeetingId(Long userId, Long meetingId);
}
