# Git Commit — Core-First (Body in Korean)

## 1) 포맷

```
Type: <핵심 변경 한 줄 요약, 영어·명령형·마침표 X>

- 부수 변경: …   # 0–10개, 필요할 때만 추가
- 부수 변경: …
- 이유: …        # 1개 필수
Refs: #123        # 선택
```

* **부수 변경**은 **없으면 전부 생략(0개)**, 많으면 **최대 10개**까지.
* 4개 이상일 땐 **코드 → 테스트 → 문서 → 인프라** 순으로 정리.
* 항목은 **중복 합치기**(주석/로그 등 사소 항목 묶기), **~72자 내** 간결히.
* 브레이킹 체인지는 **Footer**에 별도 표기.

## 2) 타입

`Feat, Fix, Build, Chore, Ci, Docs, Style, Refactor, Test, Perf`

> 범위 괄호 금지: `Feat(api): ...` ❌ → `Feat: ...` ✅

## 3) 헤더 선택 기준

* 공개 API/스키마/런타임/배포 중 **가장 영향 큰 1건**만 헤더로.
* 본문은 전부 **부수 변경**만.

## 4) 예시

### A (기능 추가, 부수 2개)

```
Feat: Enforce JWT on STOMP connect

- 부수 변경: AuthHandshakeHandler를 config 패키지로 이동
- 부수 변경: 토큰 무효 경로 통합 테스트 추가
- 이유: 무단 WebSocket 접속을 사전 차단하기 위함
Refs: #210
```

### B (버그 수정, 부수 2개)

```
Fix: Resolve Redis connection by using service name

- 부수 변경: 핸드셰이크 디버그 로그 수준 조정
- 부수 변경: compose 네트워킹 주석/README 보강
- 이유: 컨테이너에서 localhost 접근 불가 → 서비스명으로 연결 필요
Refs: #231
```

### C (성능, 부수 2개)

```
Perf: Add (room_id, created_at DESC) index for reads

- 부수 변경: JPA 쿼리를 createdAt 정렬 기준으로 단순화
- 부수 변경: 읽기 경로 k6 스모크 테스트 추가
- 이유: 고부하 시 레이턴시 및 DB 부하 감소
Refs: #275
```

### D (브레이킹 체인지, 부수 2개)

```
Feat!: Unify message schema to 'channelId'

- 부수 변경: chat-ws/chat-history DTO 마이그레이션
- 부수 변경: REST/WS 문서 갱신
- 이유: 멀티 테넌트 라우팅 준비
BREAKING CHANGE: Clients must send 'channelId' instead of 'roomId'
Refs: #300
```

### E (부수 변경 0개 예시)

```
Refactor: Inline small factory methods

- 이유: 불필요한 간접 호출 제거로 가독성 및 유지보수성 개선
Refs: #412
```

## 5) `.gitmessage.txt` (한국어 본문·유동 부수 변경)

```
# Header (required) — focus on the single core change
# Types: Feat | Fix | Build | Chore | Ci | Docs | Style | Refactor | Test | Perf
Type: <Core change in imperative, no period>

# Body (Korean)
# 부수 변경은 0~10개. 의미 있는 것만 남기고, 불필요하면 전부 삭제하세요.
# 4개 이상이면 코드 → 테스트 → 문서 → 인프라 순서 권장.
- 부수 변경: 
- 부수 변경: 
- 부수 변경: 
- 부수 변경: 
- 부수 변경: 
- 부수 변경: 
- 부수 변경: 
- 부수 변경: 
- 부수 변경: 
- 부수 변경: 
- 이유: 

Refs: #<issue>

# For breaking changes (keep keyword in English):
# BREAKING CHANGE: <impact and migration>
```

## 6) Copilot 지시문 (부수 변경 자동 결정 포함)

```
Generate a commit message that follows:

Header (English)
- "Type: Title" (imperative, capitalized, no period).
- Allowed types: Feat, Fix, Build, Chore, Ci, Docs, Style, Refactor, Test, Perf.
- No scopes in parentheses.

Body (Korean)
- Add 0–10 "부수 변경: ..." bullets for ancillary (non-core) changes ONLY.
- If there are no meaningful ancillary changes, output NO "부수 변경:" lines.
- Keep each bullet within ~72 characters; merge trivial items into one line.
- If 4+ bullets, order them as: 코드 → 테스트 → 문서 → 인프라.
- Include exactly one "이유: ..." line describing the purpose/effect of the core change.

Footer (optional)
- "Refs: #<number>".
- If breaking, include: "BREAKING CHANGE: ...".

Selection logic
- Determine the number of "부수 변경" bullets from the diff.
- Prioritize by impact and user visibility; cap at 10.
```

## 7) 체크리스트 (업데이트)

* [ ] 헤더는 영어·핵심 한 줄?
* [ ] **부수 변경 0~10개**(없으면 생략, 많으면 10개 이내)?
* [ ] 4개 이상이면 **코드→테스트→문서→인프라** 순서?
* [ ] 각 항목 ~72자, 중복/사소 항목은 병합?
* [ ] `이유:` 1개로 목적/효과만?
* [ ] (해당 시) `BREAKING CHANGE:` 포함?