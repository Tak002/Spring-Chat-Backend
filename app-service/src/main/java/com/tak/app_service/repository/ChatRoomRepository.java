package com.tak.app_service.repository;

import com.tak.app_service.entity.ChatRoom;
import com.tak.app_service.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    Optional<ChatRoom> findByMeeting(Meeting meeting);
}
