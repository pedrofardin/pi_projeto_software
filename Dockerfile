FROM eclipse-temurin:21-jre-alpine

COPY target/pi_proj_soft-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]