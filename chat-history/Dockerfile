# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk AS build
WORKDIR /src
COPY . ./
RUN chmod +x gradlew
RUN ./gradlew :chat-history:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
# EXPOSE 생략(웹 미노출)
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS=""
COPY --from=build /src/chat-history/build/libs/*.jar app.jar
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
