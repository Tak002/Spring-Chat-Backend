package com.tak.app_service.repository;

import com.tak.app_service.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
