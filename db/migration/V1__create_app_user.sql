CREATE EXTENSION IF NOT EXISTS citext;
CREATE TABLE app_user (
                          id               BIGSERIAL PRIMARY KEY,
                          email            CITEXT NOT NULL UNIQUE,
                          password_hash    TEXT,                                 -- OAuth-only면 NULL
                          status           TEXT NOT NULL DEFAULT 'active' CHECK (status IN ('active','suspended','deleted')),
                          role             TEXT NOT NULL DEFAULT 'user' CHECK (role IN ('user','admin')),
                          created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
                          updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
                          deleted_at       TIMESTAMPTZ
);