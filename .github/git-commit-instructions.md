아래 내용을 `git-commit-instructions.md`로 저장해 사용하세요. (형식 #2: **짧은 본문 버전**)

---

# Git Commit Instructions

팀 공통 규칙으로, **한 줄 헤더 + 1–3줄 불릿 본문**을 사용합니다.
모든 메시지는 **영어**, **명령형**, **간결함**을 기본으로 합니다.

## 1) Format

```
Type: Title

- What changed: …
- Why: …
Refs: #123
```

* **Header(필수)**: `Type: Title`

    * Title는 **명령형**, **첫 글자 대문자**, **끝에 마침표 X**
* **Body(권장)**: 1–3줄 불릿로 요지만

    * `What changed`: 코드 관점 변경 요약
    * `Why`: 배경/의도(버그 재현 조건, 성능/설계 이유 등)
* **Footer(선택)**: `Refs: #issue` / `BREAKING CHANGE: …`

## 2) Allowed Types

`Feat, Fix, Build, Chore, Ci, Docs, Style, Refactor, Test, Perf`

> 범위(scope) 괄호 사용 금지. 예) `Feat(api): …` ❌ → `Feat: …` ✅

## 3) Writing Rules

* 한 커밋 = 한 목적(“원인-변경-결과”가 일관되도록)
* 구현 상세보다는 **효과/의도** 중심 (리뷰어가 “왜”를 바로 파악)
* 숫자/파라미터/임계치 변경은 구체적으로
* 한국어 금지, 이모지/마침표/문장부호 남용 금지
* 브레이킹 체인지가 있으면 반드시 명시

## 4) Examples

### Feat

```
Feat: Add JWT handshake validation for WebSocket

- What changed: Validate token in AuthHandshakeHandler before STOMP connect
- Why: Prevent unauthorized ws connections across chat-ws
Refs: #210
```

### Fix

```
Fix: Resolve Redis listener startup failure on Docker network

- What changed: Set spring.data.redis.host to service name 'redis'
- Why: Container could not reach localhost -> use compose DNS
Refs: #231
```

### Build

```
Build: Add multi-module bootJar tasks to CI

- What changed: Build :chat-ws and :chat-history jars in pipeline
- Why: Ensure deploy artifacts for both modules
Refs: #245
```

### Refactor

```
Refactor: Extract RedisMessageListener configuration

- What changed: Move listener wiring into dedicated @Configuration class
- Why: Reduce context coupling and simplify slice tests
```

### Perf

```
Perf: Reduce broadcast fanout allocations

- What changed: Reuse object mapper and pre-serialize static headers
- Why: Lower GC pressure under 5k concurrent ws clients
```

### Docs

```
Docs: Clarify docker-compose dev vs prod usage

- What changed: Add examples for merge -f base -f dev and service targets
- Why: Prevent misuse in IntelliJ run configs
```

### Ci

```
Ci: Push image with Buildx cache and tag by SHA

- What changed: Enable inline cache and add gh.sha tag
- Why: Speed up rebuilds and allow reproducible rollbacks
```

### Test

```
Test: Add WebSocket integration test for room join/leave

- What changed: k6 smoke + Spring messaging integration tests
- Why: Guard against regressions in handshake path
```

## 5) BREAKING CHANGE

중요 API/스키마 변경 시 본문에 명시하세요.

```
Feat!: Unify message schema across chat-ws and chat-history

- What changed: Replace 'roomId' with 'channelId' in payload
- Why: Prepare for multi-tenant routing
BREAKING CHANGE: Clients must send 'channelId' field instead of 'roomId'
```

## 6) Quick Template (.gitmessage.txt)

레포 루트에 저장 후:

```bash
git config commit.template .gitmessage.txt
```

`./.gitmessage.txt` 내용:

```
# Header (required)
# Types: Feat | Fix | Build | Chore | Ci | Docs | Style | Refactor | Test | Perf
Type: <Title in imperative mood, no period>

# Body (1–3 bullets; keep concise)
- What changed: 
- Why: 
Refs: #<issue>

# For breaking changes, add:
# BREAKING CHANGE: <impact and migration>
```

## 7) Copilot와 함께 쓰기 (선택)

Copilot의 **Git Commit Instructions**(Global/Workspace)에 아래를 추가하면 자동 생성 품질이 좋아집니다.

```
Generate commit messages using this format:

1) Header: "Type: Title"
   - Types: Feat, Fix, Build, Chore, Ci, Docs, Style, Refactor, Test, Perf
   - No parentheses or scope. Title in imperative mood, capitalized, no final period.

2) Body: 1–3 bullet lines
   - "- What changed: ..."
   - "- Why: ..."
   - Optional: "Refs: #123"
   - If breaking: add "BREAKING CHANGE: ..."

Write in concise English. Prefer intent over diff detail.
```

## 8) Anti-patterns (Don’t)

* `Update`, `Fix bug`, `WIP`, `misc` 같은 모호한 제목
* 한국어/이모지/마침표로 끝나는 제목
* 한 커밋에 관계없는 변경 묶기
* 본문 없는 대규모 변경(리뷰어가 “왜”를 알 수 없음)

---

이 문서대로 커밋하면, 히스토리 검색성/리뷰 속도/릴리즈 노트 품질이 확실히 좋아집니다.
