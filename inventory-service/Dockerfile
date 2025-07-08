FROM eclipse-temurin:21

WORKDIR /app

COPY target/inventory-service-*.jar /app/inventory-service.jar

ENTRYPOINT ["java", "-jar", "inventory-service.jar"]