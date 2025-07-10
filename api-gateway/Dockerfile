FROM eclipse-temurin:21

WORKDIR /app

COPY target/api-gateway*.jar /app/api-gateway.jar

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]