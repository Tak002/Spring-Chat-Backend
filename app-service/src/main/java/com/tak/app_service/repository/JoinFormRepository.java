package com.tak.app_service.repository;

import com.tak.app_service.entity.JoinForm;
import com.tak.app_service.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoinFormRepository extends JpaRepository<JoinForm, Long> {

    Optional<JoinForm> findByMeeting(Meeting meeting);
}
