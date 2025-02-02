# Use an official OpenJDK runtime as the base image
FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/shopping-cart-*.jar shopping-cart.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/shopping-cart.jar"]
