package com.tak.chat_history.chatMessage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    // 방별 최신순 페이지 조회 (무한 스크롤 첫 페이지 등)
    Page<ChatMessage> findByRoomIdOrderByCreatedAtDesc(String roomId, Pageable pageable);

    // 앵커 createdAt 이전 구간 조회 (seek pagination)
    Slice<ChatMessage> findByRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
            String roomId, OffsetDateTime before, Pageable pageable
    );

    // 본문 부분 검색 (ILIKE 유사) - 인덱스는 트라이그램 쓰면 더 좋지만, 스키마에 맞춰 간단히 LIKE
    @Query("""
           SELECT m
           FROM ChatMessage m
           WHERE m.roomId = :roomId
             AND LOWER(m.content) LIKE LOWER(CONCAT('%', :q, '%'))
           ORDER BY m.createdAt DESC
           """)
    Page<ChatMessage> searchInRoom(
            @Param("roomId") String roomId,
            @Param("q") String query,
            Pageable pageable
    );

    @Query(
        value = """
          SELECT *
          FROM chat_message
          WHERE room_id = :roomId
            AND (:includeDeleted = TRUE OR is_deleted = FALSE)
          ORDER BY created_at DESC
          """,
        countQuery = """
           SELECT count(*)
           FROM chat_message
           WHERE room_id = :roomId
             AND (:includeDeleted = TRUE OR is_deleted = FALSE)
           """,
        nativeQuery = true
    )
    Page<ChatMessage> findByRoomIdWithOption(
            @Param("roomId") String roomId,
            @Param("includeDeleted") boolean includeDeleted,
            Pageable pageable
    );

}
