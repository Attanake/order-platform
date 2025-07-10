FROM eclipse-temurin:21

WORKDIR /app

COPY target/auth-service*.jar /app/auth-service.jar

ENTRYPOINT ["java", "-jar", "auth-service.jar"]