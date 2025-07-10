FROM eclipse-temurin:21

WORKDIR /app

COPY target/order-service*.jar /app/order-service.jar

ENTRYPOINT ["java", "-jar", "order-service.jar"]