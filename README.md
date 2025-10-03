# 🚀 Spring WebSocket Chat Server
STOMP + Redis Pub/Sub 기반의 실시간 멀티룸 채팅 서버

[![Build Status](https://github.com/tak002/Spring-Redis-PubSub/actions/workflows/deploy.yml/badge.svg)](https://github.com/tak002/Spring-Redis-PubSub/actions)
[![Latest Release](https://img.shields.io/github/v/release/tak002/Spring-Redis-PubSub?sort=semver)](https://github.com/tak002/Spring-Redis-PubSub/releases)

## 🖼️ Demo
<img width="1731" height="598" alt="스크린샷 2025-10-03 201812" src="https://github.com/user-attachments/assets/39aa15fb-b0ce-40ed-a349-d1f18ae16425" />

## ✨ Features
- STOMP 기반 멀티룸 실시간 채팅
- Docker + GitHub Actions 기반 자동 배포
  
## 🔮 Planned
- Redis Pub/Sub을 통한 분산 서버 메시지 동기화
- JWT 인증
- JWT 기반 WebSocket 인증 완성
- 채팅 기록 저장 및 조회 API 추가
- 대규모 트래픽 부하 테스트 및 최적화


---

## 📦 폴더별 기능

### 🔹 `chat-ws`
- WebSocket 기반 실시간 메시지 송수신
- STOMP 프로토콜 지원
- Redis Pub/Sub을 통한 멀티 인스턴스 간 메시지 브로드캐스트
- 클라이언트 연결 관리 (방 입장/퇴장, Heartbeat)

### 🔹 `chat-auth`
- 사용자 회원가입 및 로그인
- JWT 토큰 발급 및 검증
- 토큰 기반 세션 인증
- 향후 OAuth2, 소셜 로그인 확장 가능

### 🔹 `chat-history`
- 메시지 저장 및 조회 API
- PostgreSQL(or MongoDB) 기반 영속성 관리
- 채팅방 단위 대화 기록 페이징 조회
- 향후 검색/필터링, 메시지 삭제 기능 확장 가능
