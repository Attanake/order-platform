FROM eclipse-temurin:21

WORKDIR /app

COPY target/auth_service_jar/auth-service.jar /app/auth-service.jar

ENTRYPOINT ["java", "-jar", "auth-service.jar"]