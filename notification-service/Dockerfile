FROM eclipse-temurin:21

WORKDIR /app

COPY target/notification-service*.jar /app/notification-service.jar

ENTRYPOINT ["java", "-jar", "notification-service.jar"]