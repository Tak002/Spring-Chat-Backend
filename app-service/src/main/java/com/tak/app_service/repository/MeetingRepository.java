package com.tak.app_service.repository;

import com.tak.app_service.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByLinkedEventId(Long eventId);

    List<Meeting> findByTitleContains(String title);
}
