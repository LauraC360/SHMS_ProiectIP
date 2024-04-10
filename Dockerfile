# Use Maven official image with JDK 11 for building the project
FROM maven:3.8.4-openjdk-17 as build

# Set the working directory in the Docker container
WORKDIR /app

# Improve rebuild performance by caching Maven dependencies
COPY ProjectStructure/demo/pom.xml .

# Copy the project source
COPY /ProjectStructure/demo/src ./src

# Package the application
RUN mvn clean package

# Use OpenJDK for running the application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
