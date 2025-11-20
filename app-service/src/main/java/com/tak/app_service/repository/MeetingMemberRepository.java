package com.tak.app_service.repository;

import com.tak.app_service.entity.MeetingMember;
import com.tak.app_service.entity.Meeting;
import com.tak.app_service.entity.enums.MeetingMemberState;
import com.tak.common.appUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {

    Optional<MeetingMember> findByMeetingAndUser(Meeting meeting, AppUser user);

    List<MeetingMember> findByMeetingAndState(Meeting meeting, MeetingMemberState state);
}
