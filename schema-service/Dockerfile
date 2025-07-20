FROM eclipse-temurin:21

WORKDIR /app

COPY target/schema-service*.jar /app/schema-service.jar

ENTRYPOINT ["java", "-jar", "schema-service.jar"]