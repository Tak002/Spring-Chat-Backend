# 1단계: 빌드 환경 (JDK 포함)
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
# 프로젝트 전체를 컨테이너 안으로 복사
COPY . .
RUN ./gradlew clean bootJar --no-daemon # gradlew로 스프링부트 실행 가능한 jar 파일 생성

# 2단계: 실행 환경 (JRE만 포함 → 더 가볍게)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
