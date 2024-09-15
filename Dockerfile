# Use Maven to build the application
FROM maven:3.9.8-eclipse-temurin-21 AS build

# Set the working directory for the build
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package

# Use a minimal Java runtime for the final image
FROM openjdk:21-jdk-slim

# Add a volume to hold the application data
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=target/blog-0.0.1-SNAPSHOT.jar

# Copy the jar file from the build stage to the final image
COPY --from=build /app/${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]