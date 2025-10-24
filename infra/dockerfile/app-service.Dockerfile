FROM eclipse-temurin:21-jre
WORKDIR /app
COPY app-service/build/libs/app-service.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
