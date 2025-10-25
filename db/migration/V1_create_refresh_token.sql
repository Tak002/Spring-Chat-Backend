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
