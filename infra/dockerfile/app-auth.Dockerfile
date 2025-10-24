FROM eclipse-temurin:21-jre
WORKDIR /app
COPY app-auth/build/libs/app-auth.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
