FROM eclipse-temurin:21-jre
WORKDIR /app
COPY chat-ws/build/libs/chat-ws.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
