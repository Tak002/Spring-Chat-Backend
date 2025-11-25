-- =========================================
-- Extensions
-- =========================================
CREATE EXTENSION IF NOT EXISTS citext;
CREATE EXTENSION IF NOT EXISTS pgcrypto; -- UUID 생성용

-- =========================================
-- Core User & Auth
-- =========================================
CREATE TABLE app_user (
                          id                BIGSERIAL PRIMARY KEY,
                          email             CITEXT NOT NULL UNIQUE,
                          password_hash     TEXT,
                          status            TEXT NOT NULL DEFAULT 'active'
                              CHECK (status IN ('active','suspended','deleted')),
                          role              TEXT NOT NULL DEFAULT 'user'
                              CHECK (role IN ('user','admin')),
                          nickname          CITEXT NOT NULL,
                          bio               TEXT,
                          visibility        TEXT,                 -- e.g. PUBLIC | ...
                          department        TEXT,                 -- 학과
                          birth_date        DATE,                 -- 생년월일
                          profile_image_id  TEXT,                 -- 나중에 media(key) FK 추가
                          email_verified    BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
                          updated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
                          deleted_at        TIMESTAMPTZ
);

-- email 인증 코드
CREATE TABLE email_verification (
                                    id          BIGSERIAL PRIMARY KEY,
                                    email       CITEXT NOT NULL,
                                    code        TEXT   NOT NULL,
                                    issued_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                                    verified_at TIMESTAMPTZ
);

-- Refresh Token (회전/무효화 추적)
CREATE TABLE refresh_token (
                               id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               user_id      BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    -- SHA-256 hex string (64 chars)
                               token_hash   CHAR(64) NOT NULL,
                               expires_at   TIMESTAMPTZ NOT NULL,
                               revoked      BOOLEAN NOT NULL DEFAULT FALSE,
                               created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 유저별 정리/조회 최적화
CREATE INDEX refresh_token_user_id_idx
    ON refresh_token(user_id);

-- 실제 인증 경로에서 가장 많이 조회될 조건만 올린 부분 인덱스
CREATE INDEX refresh_token_valid_lookup_idx
    ON refresh_token(token_hash, expires_at)
    WHERE revoked = FALSE;

-- =========================================
-- Media
-- =========================================
CREATE TABLE media (
                       id            BIGSERIAL PRIMARY KEY,      -- 1부터 증가하는 숫자
                       content_type  TEXT NOT NULL,
                       purpose       TEXT,                       -- THUMBNAIL | PROFILE | ...
                       owner_id      BIGINT REFERENCES app_user(id)
                                              ON UPDATE CASCADE
                                              ON DELETE SET NULL,
                       created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
);

-- =========================================
-- Tags
-- =========================================
CREATE TABLE tag (
                     id          BIGSERIAL PRIMARY KEY,
                     target      TEXT,                            -- 'events' | 'meetings' (선택)
                     name        CITEXT NOT NULL UNIQUE,
                     created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                     CHECK (target IS NULL OR target IN ('events','meetings'))
);

-- =========================================
-- Events
-- =========================================
CREATE TABLE event (
                       id            BIGSERIAL PRIMARY KEY,
                       owner_id      BIGINT NOT NULL REFERENCES app_user(id)
                           ON UPDATE CASCADE
                           ON DELETE CASCADE,
                       title         TEXT   NOT NULL,
                       description   TEXT,
                       start_at      TIMESTAMPTZ NOT NULL,
                       end_at        TIMESTAMPTZ,
                       place         TEXT,
                       thumbnail_id  TEXT REFERENCES media(key)
                                                ON UPDATE CASCADE
                                                ON DELETE SET NULL,
                       status        TEXT NOT NULL DEFAULT 'ACTIVE'
                           CHECK (status IN ('ACTIVE','DELETED')),
                       created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
                       updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Event-Tag (N:N)
CREATE TABLE event_tag (
                           event_id  BIGINT NOT NULL REFERENCES event(id)
                               ON UPDATE CASCADE
                               ON DELETE CASCADE,
                           tag_id    BIGINT NOT NULL REFERENCES tag(id)
                               ON UPDATE CASCADE
                               ON DELETE CASCADE,
                           PRIMARY KEY (event_id, tag_id)
);

-- =========================================
-- Meetings
-- =========================================
CREATE TABLE meeting (
                         id               BIGSERIAL PRIMARY KEY,
                         host_id          BIGINT NOT NULL REFERENCES app_user(id)
                             ON UPDATE CASCADE
                             ON DELETE CASCADE,
                         title            TEXT   NOT NULL,
                         description      TEXT,
                         date             DATE,
                         time             TIME,
                         place            TEXT,
                         max_members      INTEGER,
                         rules_json       TEXT,   -- JSON 문자열 보관 시 JSONB로 변경 가능
                         thumbnail_id     TEXT REFERENCES media(key)
                                                     ON UPDATE CASCADE
                                                     ON DELETE SET NULL,
                         linked_event_id  BIGINT REFERENCES event(id)
                                                     ON UPDATE CASCADE
                                                     ON DELETE SET NULL,
                         status           TEXT NOT NULL DEFAULT 'OPEN'
                             CHECK (status IN ('OPEN','CLOSED','CANCELLED','ENDED')),
                         created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
                         updated_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Meeting-Tag (N:N)
CREATE TABLE meeting_tag (
                             meeting_id  BIGINT NOT NULL REFERENCES meeting(id)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE,
                             tag_id      BIGINT NOT NULL REFERENCES tag(id)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE,
                             PRIMARY KEY (meeting_id, tag_id)
);

-- =========================================
-- Join (approval flow)
-- =========================================
CREATE TABLE meeting_member (
                                id           BIGSERIAL PRIMARY KEY,
                                meeting_id   BIGINT NOT NULL REFERENCES meeting(id)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
                                user_id      BIGINT NOT NULL REFERENCES app_user(id)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
                                role         TEXT   NOT NULL DEFAULT 'MEMBER'
                                    CHECK (role IN ('HOST','MEMBER')),
                                state        TEXT   NOT NULL DEFAULT 'PENDING'
                                    CHECK (state IN ('PENDING','APPROVED','REJECTED','LEFT')),
                                requested_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                                decided_at   TIMESTAMPTZ
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_meeting_member_unique
    ON meeting_member(meeting_id, user_id);

CREATE TABLE join_form (
                           id          BIGSERIAL PRIMARY KEY,
                           meeting_id  BIGINT NOT NULL REFERENCES meeting(id)
                               ON UPDATE CASCADE
                               ON DELETE CASCADE,
                           created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE join_form_question (
                                    id        BIGSERIAL PRIMARY KEY,
                                    form_id   BIGINT NOT NULL REFERENCES join_form(id)
                                        ON UPDATE CASCADE
                                        ON DELETE CASCADE,
                                    question  TEXT   NOT NULL,
                                    type      TEXT   NOT NULL CHECK (type IN ('TEXT','CHOICE','NUMBER','DATE','OTHER')),
                                    order_no  INTEGER NOT NULL
);

CREATE TABLE join_answer (
                             id           BIGSERIAL PRIMARY KEY,
                             meeting_id   BIGINT NOT NULL REFERENCES meeting(id)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE,
                             user_id      BIGINT NOT NULL REFERENCES app_user(id)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE,
                             question_id  BIGINT NOT NULL REFERENCES join_form_question(id)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE,
                             value        TEXT,
                             answered_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_join_answer_unique
    ON join_answer(meeting_id, user_id, question_id);

-- =========================================
-- Reports & Moderation
-- =========================================
CREATE TABLE report (
                        id           BIGSERIAL PRIMARY KEY,
                        reporter_id  BIGINT NOT NULL REFERENCES app_user(id)
                            ON UPDATE CASCADE
                            ON DELETE CASCADE,
                        target_type  TEXT   NOT NULL CHECK (target_type IN ('USER','MEETING','EVENT')),
                        target_id    TEXT   NOT NULL,  -- 다형 대상, 텍스트로 보관
                        reason_type  TEXT   NOT NULL CHECK (reason_type IN ('SPAM','ABUSE','OTHER')),
                        detail       TEXT,
                        status       TEXT   NOT NULL DEFAULT 'PENDING'
                            CHECK (status IN ('PENDING','DECIDED')),
                        created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_report_dedup
    ON report (reporter_id, target_type, target_id)
    WHERE status = 'PENDING';

CREATE TABLE moderation_decision (
                                     id          BIGSERIAL PRIMARY KEY,
                                     report_id   BIGINT NOT NULL REFERENCES report(id)
                                         ON UPDATE CASCADE
                                         ON DELETE CASCADE,
                                     admin_id    BIGINT NOT NULL REFERENCES app_user(id)
                                         ON UPDATE CASCADE
                                         ON DELETE SET NULL,
                                     action      TEXT   NOT NULL CHECK (action IN ('WARN','HOLD','REJECT')),
    memo        TEXT,
    decided_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- =========================================
-- Chat (history service)
-- =========================================
CREATE TABLE chat_room (
                           id          TEXT PRIMARY KEY,                 -- roomId
                           meeting_id  BIGINT REFERENCES meeting(id)
                                                  ON UPDATE CASCADE
                                                  ON DELETE SET NULL,
                           created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE chat_message (
                              id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),  -- 메시지 고유 ID
                              room_id     TEXT   NOT NULL REFERENCES chat_room(id)
                                  ON UPDATE CASCADE
                                  ON DELETE CASCADE,
                              sender_id   BIGINT NOT NULL REFERENCES app_user(id)
                                  ON UPDATE CASCADE
                                  ON DELETE SET NULL,
                              content     TEXT        NOT NULL,                               -- 메시지 본문
                              created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),                 -- 생성 시각(정렬/페이지네이션 기준)
                              edited_at   TIMESTAMPTZ,                                        -- 편집 시각(수정 시 갱신)
                              is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE                  -- 소프트 삭제 플래그
);

-- =========================================
-- Helpful secondary indexes
-- =========================================
CREATE INDEX IF NOT EXISTS ix_event_time ON event(start_at, end_at);
CREATE INDEX IF NOT EXISTS ix_meeting_status ON meeting(status);
CREATE INDEX IF NOT EXISTS ix_meeting_host ON meeting(host_id);
CREATE INDEX IF NOT EXISTS ix_tag_target ON tag(target);

-- =========================================
-- Cyclic FK 마무리: app_user.profile_image_id → media.key
-- (app_user, media 둘 다 이미 생성된 후 마지막에 FK 추가)
-- =========================================
ALTER TABLE app_user
    ADD CONSTRAINT fk_app_user_profile_image
        FOREIGN KEY (profile_image_id)
            REFERENCES media(key)
            ON UPDATE CASCADE
            ON DELETE SET NULL;
