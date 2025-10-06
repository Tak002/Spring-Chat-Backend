 ## 빌드 단계
#FROM eclipse-temurin:21-jdk AS build
#WORKDIR /app
##COPY . .
##RUN chmod +x gradlew
#
## gradle wrapper만 확실히 복사
#COPY gradlew gradlew
#COPY gradle/wrapper gradle/wrapper
#RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
#
## 빌드 스크립트/설정과 소스만 복사 (캐시 효율↑, 시크릿 유출↓)
#COPY settings.gradle settings.gradle
#COPY build.gradle build.gradle
#
#COPY chat-common/build.gradle chat-common/build.gradle
#COPY chat-common/src chat-common/src
#COPY chat-ws/build.gradle chat-ws/build.gradle
#COPY chat-ws/src chat-ws/src
#
#
#RUN ./gradlew :chat-ws:bootJar --no-daemon -x test

# 실행 단계
FROM eclipse-temurin:21-jre
WORKDIR /app
#COPY --from=build /app/chat-ws/build/libs/chat-ws.jar app.jar
COPY chat-ws/build/libs/chat-ws.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
