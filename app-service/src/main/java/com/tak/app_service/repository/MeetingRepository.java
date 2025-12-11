package com.tak.app_service.repository;

import com.tak.app_service.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByLinkedEventId(Long eventId);

    List<Meeting> findByTitleContains(String title);

    @Query("SELECT m.id FROM Meeting m WHERE m.hostId = :hostId")
    List<Long> findIdByHostId(Long hostId);
}
