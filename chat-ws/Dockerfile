# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
# 개발 중이면 테스트 생략 권장: -x test
RUN ./gradlew clean bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS=""
# 빌드 산출물 복사
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
