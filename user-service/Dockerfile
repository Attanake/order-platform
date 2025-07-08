FROM eclipse-temurin:21

WORKDIR /app

COPY target/user-service-*.jar /app/user-service.jar

ENTRYPOINT ["java", "-jar", "user-service.jar"]