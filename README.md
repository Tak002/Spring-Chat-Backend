# 🚀 Spring WebSocket Chat Server
STOMP + Redis Pub/Sub 기반의 실시간 멀티룸 채팅 서버

[![Build Status](https://github.com/tak002/Spring-Redis-PubSub/actions/workflows/deploy.yml/badge.svg)](https://github.com/tak002/Spring-Redis-PubSub/actions)
[![Latest Release](https://img.shields.io/github/v/release/tak002/Spring-Redis-PubSub?sort=semver)](https://github.com/tak002/Spring-Redis-PubSub/releases)

## 🖼️ Demo
<img width="1815" height="671" alt="스크린샷 2025-10-08 031804" src="https://github.com/user-attachments/assets/1789bc2f-7a7a-48c9-9b85-085fae18414f" />

## ✨ Features
- STOMP 기반 멀티룸 실시간 채팅
- Docker + GitHub Actions 기반 자동 배포
- Redis Pub/Sub을 통한 분산 서버 메시지 동기화
- 채팅 기록 저장 및 조회 API 추가

## 🔮 Planned
- JWT 인증
- JWT 기반 WebSocket 인증 완성
- 대규모 트래픽 부하 테스트 및 최적화
- 테스트 자동화 및 커버리지 확장(단위/통합/E2E)

---

## 📦 모듈별 기능

### 🔹 `chat-ws` : 실시간 메시지 송수신
- WebSocket 기반 실시간 메시지 송수신
- STOMP 프로토콜 지원
- Redis Pub/Sub을 통한 멀티 인스턴스 간 메시지 브로드캐스트
- 클라이언트 연결 관리 (방 입장/퇴장, Heartbeat)

### 🔹 `chat-auth` : 로그인/토큰 발급/검증
- 사용자 회원가입 및 로그인
- JWT 토큰 발급 및 검증
- 토큰 기반 세션 인증
- 향후 OAuth2, 소셜 로그인 확장 가능

### 🔹 `chat-history` : 대화 내용 저장 및 불러오기
- 메시지 저장 및 조회 API
- PostgreSQL(or MongoDB) 기반 영속성 관리
- 채팅방 단위 대화 기록 페이징 조회
- 향후 검색/필터링, 메시지 삭제 기능 확장 가능


---

## 🌿 **브랜치 전략**

### 🔧 기능 개발

* 브랜치명: **`feat/기능명`**
* 각 기능은 별도의 `feat/기능명` 브랜치에서 개발합니다.
* 개발이 완료되면 `main` 브랜치로 **PR(Pull Request)** 를 생성합니다.

---

### 🧩 코드 리뷰 및 통합

* 대상 브랜치: **`main`**
* 모든 PR은 `main`을 기준으로 생성합니다.
* 코드 리뷰 및 테스트가 완료되면 `main`에 **머지(Merge)** 합니다.

---

### 🚀 배포 및 실환경 테스트

* 배포용 브랜치: **`production`**
* 배포 시, `main`의 코드를 `production`으로 **머지**합니다.
* 배포 과정에서 필요한 환경 설정이나 추가 수정은
  `production` 브랜치에서 직접 커밋할 수 있습니다.
* 단, `production`에서 발생한 변경사항은
  반드시 다시 `main`에 **PR로 반영**하여
  두 브랜치의 이력을 일치시켜야 합니다.

---

### 🔁 브랜치 흐름 예시

```
1️⃣ feat/기능명 ─▶ main           (기능 개발 후 PR 및 머지)
2️⃣ main ─▶ production            (배포 및 실환경 테스트)
3️⃣ production ─▶ main            (배포 관련 수정사항 반영)
4️⃣ production                    (배포 진행)
```

---
