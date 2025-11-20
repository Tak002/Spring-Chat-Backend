package com.tak.app_service.repository;

import com.tak.app_service.entity.Meeting;
import com.tak.app_service.entity.enums.MeetingStatus;
import com.tak.common.appUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByHost(AppUser host);

    List<Meeting> findByStatus(MeetingStatus status);
}
