FROM eclipse-temurin:21-jre
WORKDIR /app
COPY app-media/build/libs/app-media.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
