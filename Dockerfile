# Use an official OpenJDK runtime as the base image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/shopping-cart-*.jar shopping-cart.jar

# Expose the application port (default Spring Boot port is 8080)
EXPOSE 8083

# Run the application
ENTRYPOINT ["java", "-jar", "/app/shopping-cart.jar"]
