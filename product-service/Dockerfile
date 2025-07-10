FROM eclipse-temurin:21

WORKDIR /app

COPY target/product-service*.jar /app/product-service.jar

ENTRYPOINT ["java", "-jar", "product-service.jar"]