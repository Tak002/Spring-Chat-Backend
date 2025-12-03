package com.tak.chat_history.chatMessage;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

// todo 데이터를 저장할떄, Redis에게 송신받은 순서대로 db에 저장하는게 보장이 되는가?
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findByRoomIdAndDeletedFalseOrderByCreatedAtAsc(Long roomId);
}
