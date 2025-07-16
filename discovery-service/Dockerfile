FROM eclipse-temurin:21

WORKDIR /app

COPY target/discovery-service* /app/discovery-service.jar

ENTRYPOINT ["java", "-jar", "discovery-service.jar"]