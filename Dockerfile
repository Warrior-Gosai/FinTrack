# Stage 1: Build with Maven
FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven config first for dependency caching
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Stage 2: Runtime with lightweight OpenJDK
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Use Render’s dynamic PORT
ENV PORT=8080
EXPOSE $PORT

# Start Spring Boot
ENTRYPOINT ["java","-jar","/app.jar"]
