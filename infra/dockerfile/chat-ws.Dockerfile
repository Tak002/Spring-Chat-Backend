# 빌드 단계
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :chat-ws:bootJar --no-daemon -x test

# 실행 단계
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/chat-ws/build/libs/chat-ws.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
