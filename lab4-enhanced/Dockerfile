# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/lab4-0.0.1-SNAPSHOT.jar /app/kotlin-app.jar

# Run the application
CMD ["java", "-jar", "/app/kotlin-app.jar"]
