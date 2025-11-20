-- 1) 새 컬럼 추가 (nullable 로 먼저 추가)
ALTER TABLE meeting
    ADD COLUMN gender TEXT CHECK (gender IN ('ANY', 'MALE', 'FEMALE')),
    ADD COLUMN min_age INTEGER,
    ADD COLUMN max_age INTEGER;
UPDATE meeting
SET gender = 'ANY'
WHERE gender IS NULL;

ALTER TABLE meeting
    ALTER COLUMN gender SET NOT NULL;

ALTER TABLE meeting
DROP COLUMN rules_json;
