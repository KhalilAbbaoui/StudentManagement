# Base image with JDK 17
FROM openjdk:17-jdk-slim

# Copy the built JAR file into the container
COPY target/StudentManagement-1.0-SNAPSHOT.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
