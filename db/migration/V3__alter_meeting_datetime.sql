
-- 1) 새 컬럼 추가 (nullable 로 먼저 추가)
ALTER TABLE meeting
    ADD COLUMN start_at TIMESTAMP,
    ADD COLUMN end_at   TIMESTAMP;

-- 2) 기존 date + time 값으로 start_at / end_at 채우기
--    time 이 NULL일 수도 있으니 COALESCE 사용
UPDATE meeting
SET start_at = date::timestamp
    + COALESCE(time, '00:00'::time),
    end_at   = date::timestamp
        + COALESCE(time, '00:00'::time);

-- 필요하다면 end_at 을 다른 로직으로 설정 (ex. +1시간)
-- UPDATE meeting
-- SET end_at = start_at + INTERVAL '1 hour'
-- WHERE end_at IS NULL;

-- 3) description / place 가 NULL인 행을 임시 값으로 채우기
UPDATE meeting
SET description = ''
WHERE description IS NULL;

UPDATE meeting
SET place = ''
WHERE place IS NULL;

-- 4) NOT NULL 제약 추가
ALTER TABLE meeting
    ALTER COLUMN start_at SET NOT NULL,
ALTER COLUMN end_at   SET NOT NULL,
    ALTER COLUMN description SET NOT NULL,
    ALTER COLUMN place        SET NOT NULL;

-- 5) 기존 date / time 컬럼 삭제
ALTER TABLE meeting
DROP COLUMN date,
    DROP COLUMN time;
