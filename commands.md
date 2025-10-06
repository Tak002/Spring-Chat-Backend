# Chat-WS & Chat-History — Commands Cheat Sheet

> 프로젝트 구조: 루트 Gradle, 멀티모듈(`chat-ws`, `chat-history`), `infra/`에 compose 파일( base/dev/prod )

---

## 1) Gradle (루트에서 실행)

### 공통

* Unix/Mac: `./gradlew ...` / Windows PowerShell: `./gradlew.bat ...`

### 전체 빌드 & 모듈별 JAR

```bash
./gradlew clean build -x test                              
./gradlew :chat-ws:bootJar :chat-history:bootJar
./gradlew :chat-ws:bootJar :chat-history:bootJar -x test
./gradlew :chat-ws:build -x test
./gradlew :chat-history:build -x test
```

### 로컬 실행(프로필)

```bash
./gradlew :chat-ws:bootRun -Dspring.profiles.active=dev
./gradlew :chat-history:bootRun -Dspring.profiles.active=dev
```

### 테스트

```bash
./gradlew test                                      # 전체 테스트
./gradlew :chat-ws:test --tests "*SomeTestClass*"   # 특정 테스트
```

### 캐시/잔여 설정 이슈 정리(삭제한 모듈 빌드 시도 등)

```bash
./gradlew --stop
./gradlew clean
# settings.gradle 의 include 정리 후
./gradlew build
```

---

## 2) Docker Compose (개발: base+dev / 배포: base+prod)

> 예시 파일: `infra/docker-compose.base.yml` + `infra/docker-compose.dev.yml` (또는 `prod.yml`)

### 빌드

```bash
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml build app-chat-ws app-chat-history
# 캐시 없이
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml build --no-cache --pull app-chat-ws app-chat-history
```

### 기동 / 중지

```bash
# 특정 서비스만 기동
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml up -d app-chat-ws app-chat-history
# 전체(의존 포함) 기동
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml up -d
# 중지 (네트워크/볼륨 유지)
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml down
# 볼륨까지 정리 (데이터 초기화)
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml down -v
```

### 상태/로그/접속

```bash
# 상태
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml ps
# 로그 팔로우
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml logs -f app-chat-ws app-chat-history
# 컨테이너 쉘
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml exec app-chat-ws sh
# 환경변수 확인
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml exec app-chat-ws sh -lc 'printenv | sort'
```

### 의존 서비스 점검 (Redis/Postgres)

```bash
# Redis PING (비번 사용 시)
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml exec redis redis-cli -a "$REDIS_PASSWORD" ping

# Postgres 접속/점검
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml exec postgres psql -U localUser -d localDB -c '\dt'
```

### 단일 서비스만 재배포(이미지 교체)

```bash
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.prod.yml pull app-chat-ws app-chat-history
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.prod.yml up -d --no-deps app-chat-ws app-chat-history
```

### 정리(불필요 리소스)

```bash
docker image prune -f
docker container prune -f
docker system prune -f
```

---

## 3) 빠른 레시피

### 개발 ① 의존(디비/캐시) 먼저, 앱은 로컬 실행

```bash
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml up -d postgres redis
./gradlew :chat-ws:bootRun -Dspring.profiles.active=dev
```

### 개발 ② 전부 컨테이너

```bash
./gradlew :chat-ws:bootJar :chat-history:bootJar -x test

docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml build app-chat-ws app-chat-history

docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml up -d app-chat-ws app-chat-history

docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml logs -f app-chat-ws
```

### 배포(이미지 교체식)

```bash
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.prod.yml up -d postgres redis
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.prod.yml pull app-chat-ws app-chat-history
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.prod.yml up -d --no-deps app-chat-ws app-chat-history
```

---

## 4) 네트워킹 메모

* 컨테이너 ↔ 컨테이너: **서비스명**으로 접근

    * `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/localDB`
    * `SPRING_DATA_REDIS_HOST=redis`
* 호스트 ↔ 컨테이너: 포트 매핑 사용 (예: 브라우저 `http://localhost:8080`)
* EC2 컨테이너 → 외부/다른 호스트: 해당 호스트의 IP/도메인 사용(사설 IP면 VPC 내부 통신)

---

## 5) 환경변수 스니펫

```env
SPRING_PROFILES_ACTIVE=dev
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/localDB
SPRING_DATASOURCE_USERNAME=localUser
SPRING_DATASOURCE_PASSWORD=secret
SPRING_DATA_REDIS_HOST=redis
SPRING_DATA_REDIS_PORT=6379
REDIS_PASSWORD=changeme
APP_PORT=8080
WS_ENDPOINT=http://localhost:8080/ws-sockjs
```

---

## 6) 자주 보는 오류 & 빠른 체크리스트

* **DB/Redis 연결 실패**: 서비스 기동 여부(`ps`), 포트/호스트, 비밀번호 환경변수 확인
* **Hibernate Dialect/부트 실패**: DB URL/드라이버, `spring.jpa.properties` 확인
* **삭제한 모듈 빌드 시도**: `settings.gradle`에서 include 제거 후 `--stop && clean`
* **윈도우에서 `./gradlew` 인식 안 됨**: PowerShell에서 `./gradlew.bat ...` 사용

---

