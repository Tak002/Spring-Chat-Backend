-- =========================================
-- Dummy data: Users (profile_image_id는 일단 NULL)
-- =========================================
INSERT INTO app_user
(id, email, password_hash, nickname, bio, visibility, department, birth_date, profile_image_id, email_verified)
VALUES
    (1, 'alice@uni.ac.kr',
     'hashed_pw_alice',
     'Alice',
     '알고리즘 스터디를 좋아하는 3학년입니다.',
     'PUBLIC',
     '컴퓨터공학과',
     '2000-01-15',
     NULL,
     TRUE),
    (2, 'bob@uni.ac.kr',
     'hashed_pw_bob',
     'Bob',
     '보드게임과 친목 모임을 좋아합니다.',
     'PUBLIC',
     '전자공학과',
     '1999-07-03',
     NULL,
     TRUE),
    (3, 'admin@uni.ac.kr',
     'hashed_pw_admin',
     'Admin',
     '서비스 전체 운영을 담당하는 관리자 계정입니다.',
     'PUBLIC',
     '학생지원팀',
     '1995-05-10',
     NULL,
     TRUE);

-- =========================================
-- Dummy data: Media (owner_id는 이제 유저가 있으니 FK OK)
-- =========================================
-- INSERT INTO media (key, content_type, purpose, owner_id)
-- VALUES
--     ('img_profile_1',    'image/jpeg', 'PROFILE',   1),
--     ('img_profile_2',    'image/jpeg', 'PROFILE',   2),
--     ('img_profile_admin','image/png',  'PROFILE',   3),
--     ('img_event_1',      'image/jpeg', 'THUMBNAIL', 1),
--     ('img_meeting_1',    'image/jpeg', 'THUMBNAIL', 1);

-- =========================================
-- Users 의 profile_image_id 를 media.key 와 연결
-- =========================================
-- UPDATE app_user SET profile_image_id = 'img_profile_1'    WHERE id = 1;
-- UPDATE app_user SET profile_image_id = 'img_profile_2'    WHERE id = 2;
-- UPDATE app_user SET profile_image_id = 'img_profile_admin' WHERE id = 3;

-- =========================================
-- Dummy data: Tags
-- =========================================
INSERT INTO tag (id, target, name)
VALUES
    (1, 'events',   '스터디'),
    (2, 'meetings', '친목'),
    (3, NULL,       '게임');

-- =========================================
-- Dummy data: Events
-- =========================================
INSERT INTO event
(id, owner_id, title, description, start_at, end_at, place, thumbnail_id, status)
VALUES
    (1,
     1,
     '알고리즘 스터디 OT',
     '한 학기 동안 진행될 알고리즘 스터디의 오리엔테이션입니다.',
     '2025-11-20 19:00:00+09',
     '2025-11-20 21:00:00+09',
     '공학관 101호',
     null,
     'ACTIVE'),
    (2,
     2,
     '보드게임 번개 모임',
     '시험 끝난 기념으로 가볍게 보드게임을 즐기는 번개 모임입니다.',
     '2025-11-22 14:00:00+09',
     '2025-11-22 18:00:00+09',
     '학생회관 3층 동아리방',
     NULL,
     'ACTIVE');

-- Event-Tag 매핑
INSERT INTO event_tag (event_id, tag_id)
VALUES
    (1, 1),
    (1, 3),
    (2, 2),
    (2, 3);

-- =========================================
-- Dummy data: Meetings
-- =========================================
INSERT INTO meeting
(id, host_id, title, description, date, time, place,
 max_members, rules_json, thumbnail_id, linked_event_id, status)
VALUES
    (1,
     1,
     '알고리즘 스터디 1주차',
     '기본적인 그리디/정렬 문제를 같이 풀어보는 첫 모임입니다.',
     '2025-11-21',
     '18:00:00',
     '공학관 201호',
     6,
     '{"late_policy":"10분까지는 인정","no_show":"2회 이상 무단 결석 시 추방"}',
     null,
     1,
     'OPEN'),
    (2,
     2,
     '보드게임 친목 모임',
     '가볍게 친해지는 것을 목표로 한 보드게임 정기 모임입니다.',
     '2025-11-23',
     '15:00:00',
     '학생회관 3층 동아리방',
     8,
     '{"game_types":["전략","파티"],"no_gambling":true}',
     NULL,
     2,
     'OPEN');

-- Meeting-Tag 매핑
INSERT INTO meeting_tag (meeting_id, tag_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 2),
    (2, 3);

-- =========================================
-- Dummy data: Meeting members
-- =========================================
INSERT INTO meeting_member
(meeting_id, user_id, role, state, requested_at, decided_at)
VALUES
    (1, 1, 'HOST',   'APPROVED', '2025-11-15 10:00:00+09', '2025-11-15 10:00:00+09'),
    (1, 2, 'MEMBER', 'APPROVED', '2025-11-16 12:00:00+09', '2025-11-16 13:00:00+09'),
    (2, 2, 'HOST',   'APPROVED', '2025-11-16 09:00:00+09', '2025-11-16 09:00:00+09'),
    (2, 1, 'MEMBER', 'PENDING',  '2025-11-17 20:30:00+09', NULL);

-- =========================================
-- Dummy data: Join form & questions
-- =========================================
INSERT INTO join_form (id, meeting_id)
VALUES
    (1, 1),
    (2, 2);

INSERT INTO join_form_question
(id, form_id, question, type, order_no)
VALUES
    (1, 1, '이 스터디에 지원하게 된 동기를 적어주세요.', 'TEXT',   1),
    (2, 1, '알고리즘 문제 풀이 경험(년수)을 숫자로 적어주세요.', 'NUMBER', 2),
    (3, 2, '좋아하는 보드게임/장르를 적어주세요.', 'TEXT', 1);

-- =========================================
-- Dummy data: Join answers
-- =========================================
INSERT INTO join_answer
(meeting_id, user_id, question_id, value)
VALUES
    (1, 2, 1, '알고리즘 실력을 향상시키고 싶고, 꾸준히 문제를 풀 동료를 찾고 싶습니다.'),
    (1, 2, 2, '1');

-- =========================================
-- Dummy data: Email verification
-- =========================================
INSERT INTO email_verification
(email, code, issued_at, verified_at)
VALUES
    ('newuser@uni.ac.kr', '123456', '2025-11-18 10:00:00+09', NULL),
    ('alice@uni.ac.kr',   'ALICE1',  '2025-11-01 09:00:00+09', '2025-11-01 09:05:00+09');

-- =========================================
-- Dummy data: Reports & moderation
-- =========================================
INSERT INTO report
(id, reporter_id, target_type, target_id, reason_type, detail, status, created_at)
VALUES
    (1, 2, 'MEETING', '1', 'SPAM',
     '동일한 내용의 스터디 홍보가 여러 번 올라온 것 같습니다.',
     'PENDING',
     '2025-11-18 11:00:00+09'),
    (2, 1, 'MEETING', '2', 'ABUSE',
     '모임 채팅에서 부적절한 발언이 있었습니다.',
     'DECIDED',
     '2025-11-17 21:00:00+09');

INSERT INTO moderation_decision
(report_id, admin_id, action, memo, decided_at)
VALUES
    (2,
     3,
     'WARN',
     '호스트에게 경고 메일 발송 및 추후 모니터링 예정.',
     '2025-11-18 09:30:00+09');

-- =========================================
-- Dummy data: Chat rooms & messages
-- =========================================
INSERT INTO chat_room
(id, meeting_id, created_at)
VALUES
    ('meeting-1-main', 1, '2025-11-18 09:00:00+09'),
    ('meeting-2-main', 2, '2025-11-18 09:10:00+09');

INSERT INTO chat_message
(room_id, sender_id, content, is_deleted)
VALUES
    ('meeting-1-main', 1,
     '안녕하세요, 이번 주 1주차 스터디에서는 그리디 문제 3개 정도 풀 예정입니다.', FALSE),
    ('meeting-1-main', 2,
     '네, 참여하겠습니다! 필요한 준비물이 있을까요?', FALSE),
    ('meeting-2-main', 2,
     '오늘 보드게임 모임은 3시에 시작합니다. 시간 맞춰 와 주세요!', FALSE);
