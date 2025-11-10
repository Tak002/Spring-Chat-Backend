CREATE EXTENSION IF NOT EXISTS pgcrypto; -- 추천: UUID 생성용 (pgcrypto)

CREATE TABLE chat_message (
                              id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),  -- 메시지 고유 ID
                              room_id     TEXT        NOT NULL,                               -- 방 ID (문자열/UUID 모두 수용)
                              sender      TEXT        NOT NULL,                               -- 보낸 사람 식별자(닉/유저ID)
                              content     TEXT        NOT NULL,                               -- 메시지 본문
                              created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),                 -- 생성 시각(정렬/페이지네이션 기준)
                              edited_at   TIMESTAMPTZ,                                        -- 편집 시각(수정 시 갱신)
                              is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE                 -- 소프트 삭제 플래그
);

CREATE INDEX IF NOT EXISTS idx_chat_message__room_created
    ON chat_message (room_id, created_at DESC);