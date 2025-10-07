# Chat-WS & Chat-History — Core Commands Cheat Sheet (DEV 중심)

> 구성: 루트 Gradle 멀티모듈(`chat-ws`, `chat-history`), Compose는 `infra/docker-compose.base.yml` + `infra/docker-compose.dev.yml` 조합

---

## 🔝 자주 쓰는 명령

### 1) 앱만 빠르게 재배포 (JAR 선빌드 → 이미지 재빌드 → 앱만 재기동)

```bash
./gradlew :chat-ws:bootJar :chat-history:bootJar --no-daemon -x test
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml build --parallel app-chat-ws app-chat-history
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml up -d --no-deps app-chat-ws app-chat-history
```

* 설명: 로컬에서 JAR 생성 후, 두 앱 이미지만 재빌드·무중단 재기동.

### 2) 앱만(no cache) 강제 재배포

```bash
./gradlew :chat-ws:bootJar :chat-history:bootJar --no-daemon -x test
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml build --no-cache --pull app-chat-ws app-chat-history
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml up -d --no-deps app-chat-ws app-chat-history
```

* 설명: 캐시 무시하고 이미지 재빌드 후 앱만 교체. (의존성/레이어 꼬임 해결)

### 3) 모든 서비스 한번에 시작(처음 세팅/환경 변경 후)

```bash
./gradlew :chat-ws:bootJar :chat-history:bootJar --no-daemon -x test
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml up -d --build
```

* 설명: DB/Redis 포함 전부 기동(+필요 시 앱 빌드).

---

## 🧩 개발 편의 세트

### 상태/로그/접속

```bash
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml ps
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml logs -f app-chat-ws app-chat-history
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml exec app-chat-ws sh
```

* 설명: 서비스 상태/로그 팔로우/컨테이너 쉘 접속.

### 의존 서비스만 먼저 올리기 (로컬 DB/Redis)

```bash
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml up -d postgres redis
```

* 설명: 앱은 로컬에서 직접 `bootRun` 하거나, 나중에 따로 띄울 때 유용.

### 정리(리소스 청소)

```bash
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml down
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml down -v
docker image prune -f && docker container prune -f && docker system prune -f
```

* 설명: 컨테이너/네트워크/이미지/볼륨 정리(※ `-v`는 데이터 초기화 주의).

---

## 🧪 Gradle (루트에서)

```bash
./gradlew :chat-ws:bootJar :chat-history:bootJar -x test
./gradlew :chat-ws:test
./gradlew :chat-history:test
```

* 설명: JAR 생성 및 모듈별 테스트.

---

## 🗃️ 데이터베이스/캐시 퀵 점검

```bash
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml exec postgres psql -U localUser -d localDB -c '\dt'
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml exec redis redis-cli -a "$REDIS_PASSWORD" PING
```

* 설명: Postgres 테이블 확인 / Redis 연결 확인.

---

## 📝 사용 팁

* **변경 반영의 핵심은 JAR 선빌드**: dev 구성의 Dockerfile이 “JAR만 COPY”하므로, 항상 `bootJar → compose build → up` 순서로.
* **no-cache 상황**: 레이어 꼬임/의존성 변경 의심 시 ②번 루틴으로 강제 재빌드.
* **컨테이너 이름**: Compose의 서비스명(`app-chat-ws`, `app-chat-history`)으로 exec/logs/ps를 다룬다.
