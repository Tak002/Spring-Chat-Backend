# 빌드 단계
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :chat-history:bootJar --no-daemon -x test

# 실행 단계
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/chat-history/build/libs/chat-history.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
