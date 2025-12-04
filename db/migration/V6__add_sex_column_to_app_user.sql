-- app_user 테이블에 sex 컬럼 추가
ALTER TABLE app_user
    ADD COLUMN sex TEXT;

-- male / female만 허용하는 CHECK 제약 추가
ALTER TABLE app_user
    ADD CONSTRAINT chk_app_user_sex
        CHECK (sex IN ('male', 'female') OR sex IS NULL);
