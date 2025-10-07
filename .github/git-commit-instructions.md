# Git Commit — Core-First (Body in Korean)

## 1) 포맷

```
Type: <핵심 변경 한 줄 요약, 영어·명령형·마침표 X>

- 부수 변경: …
- 부수 변경: …
- 이유: …
Refs: #123
```

* **Header(영어, 필수)**: `Type: Title` (명령형, 첫 글자 대문자, 마침표 X)
* **Body(한국어, 필수)**:

  * `부수 변경:` 핵심 외 변경만 1–3줄
  * `이유:` 헤더(핵심 변경)의 목적/효과
* **Footer(영어, 선택)**: `Refs: #issue`, `BREAKING CHANGE: …`  ← 표준 키워드 호환성 때문에 영어 유지 권장

## 2) 타입 (동일)

`Feat, Fix, Build, Chore, Ci, Docs, Style, Refactor, Test, Perf`

> 범위 괄호 금지: `Feat(api): ...` ❌ → `Feat: ...` ✅

## 3) 헤더 선택 기준 (요약)

* 공개 API/스키마/런타임 동작/배포 중 **가장 영향 큰 1건**을 헤더로.
* 본문은 전부 **부수 변경**만.

## 4) 예시

### A (기능 추가)

```
Feat: Enforce JWT on STOMP connect

- 부수 변경: AuthHandshakeHandler를 config 패키지로 이동
- 부수 변경: 토큰 무효 경로 통합 테스트 추가
- 이유: 무단 WebSocket 접속을 사전 차단하기 위함
Refs: #210
```

### B (버그 수정)

```
Fix: Resolve Redis connection by using service name

- 부수 변경: 핸드셰이크 디버그 로그 수준 조정
- 부수 변경: compose 네트워킹 주석/README 보강
- 이유: 컨테이너에서 localhost에 접근 불가 → 서비스명으로 연결 필요
Refs: #231
```

### C (성능)

```
Perf: Add (room_id, created_at DESC) index for reads

- 부수 변경: JPA 쿼리를 createdAt 정렬 기준으로 단순화
- 부수 변경: 읽기 경로 k6 스모크 테스트 추가
- 이유: 고부하 시 레이턴시 및 DB 부하 감소
Refs: #275
```

### D (브레이킹 체인지)

```
Feat!: Unify message schema to 'channelId'

- 부수 변경: chat-ws/chat-history DTO 마이그레이션
- 부수 변경: REST/WS 문서 갱신
- 이유: 멀티 테넌트 라우팅 준비
BREAKING CHANGE: Clients must send 'channelId' instead of 'roomId'
Refs: #300
```

## 5) `.gitmessage.txt` (한국어 본문 버전)

```
# Header (required) — focus on the single core change
# Types: Feat | Fix | Build | Chore | Ci | Docs | Style | Refactor | Test | Perf
Type: <Core change in imperative, no period>

# Body (Korean; only ancillary changes; 1–3 bullets)
- 부수 변경: 
- 부수 변경: 
- 이유: 
Refs: #<issue>

# For breaking changes (keep keyword in English):
# BREAKING CHANGE: <impact and migration>
```

## 6) Copilot 지시문 (업데이트)

```
Generate commit messages with:
- Header in English: "Type: Title" (imperative, capitalized, no period).
- Body in Korean with bullets:
  - "부수 변경: ..." lines for ancillary changes only (1–3 lines).
  - One "이유: ..." line explaining the purpose/effect of the core change.
- Optional "Refs: #123".
- If breaking, include: "BREAKING CHANGE: ...".
- Allowed types: Feat, Fix, Build, Chore, Ci, Docs, Style, Refactor, Test, Perf.
- Do NOT include scopes in parentheses. Keep it concise.
```

## 7) 체크리스트

* [ ] 헤더는 영어·핵심 한 줄?
* [ ] 본문은 한국어·부수 변경만?
* [ ] `이유:`가 핵심 변경의 목적/효과만 설명?
* [ ] (해당 시) `BREAKING CHANGE:` 포함?
