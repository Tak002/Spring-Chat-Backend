# rebuild-one.ps1
# 사용법:  .\rebuild-one.ps1
# 단계:
#  1) 모듈 선택 (번호 or 이름)
#  2) 선택한 모듈만 gradle bootJar → docker build → docker up
#  3) 실패하면 즉시 중단

# 1. 선택지 정의
$modules = @(
    @{ num = "1"; name = "chat-ws" }
    @{ num = "2"; name = "chat-history" }
    @{ num = "3"; name = "app-service" }
    @{ num = "4"; name = "app-media" }
    @{ num = "5"; name = "app-auth" }
)

Write-Host "어느 모듈을 재빌드/재시작할까?"
foreach ($m in $modules) {
    Write-Host ("{0}. {1}" -f $m.num, $m.name)
}

# 2. 입력 받기
$choice = Read-Host "번호나 모듈명을 입력하세요"

# 숫자로 골랐든 이름으로 골랐든 매칭해서 실제 모듈명 뽑기
$selected = $modules |
        Where-Object { $_.num -eq $choice -or $_.name -eq $choice } |
        Select-Object -First 1

if (-not $selected) {
    Write-Host "⚠ 잘못된 입력이야. 종료할게."
    return
}

$moduleName = $selected.name
Write-Host ""
Write-Host "➡ 선택된 모듈: $moduleName"
Write-Host ""

# Gradle task 문자열(":app-service:bootJar" 이런 식)을 만들어줌
$gradleTask = ":" + $moduleName + ":bootJar"

# 3. Gradle 빌드 (해당 모듈만 JAR 생성)
Write-Host "==> Gradle bootJar 실행 중 ($gradleTask)"
& ./gradlew $gradleTask --no-daemon -x test
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Gradle 실패. 중단할게."
    return
}

# 4. Docker build (해당 서비스만 이미지 빌드)
Write-Host "==> docker compose build 실행 중 ($moduleName)"
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml build $moduleName
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Docker build 실패. 중단할게."
    return
}

# 5. Docker up (해당 컨테이너만 재시작)
Write-Host "==> docker compose up (no-deps, $moduleName)"
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml up -d --no-deps $moduleName
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ docker compose up 실패."
    return
}
# 6. reverse-proxy만 간단 재시작
Write-Host "==> reverse-proxy 재시작"
docker compose -f infra/docker-compose.base.yml -f infra/docker-compose.dev.yml restart reverse-proxy
if ($LASTEXITCODE -ne 0) { Write-Host "❌ reverse-proxy 재시작 실패."; return }

Write-Host ""
Write-Host "✅ 완료: $moduleName 리빌드 & 재시작 끝."
