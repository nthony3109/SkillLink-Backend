
# Use Java 21 base image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory inside the container
WORKDIR /app

# Copy your built JAR into the container
COPY target/skillLink-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for Spring Boot
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
