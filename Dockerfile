# ---------- Stage 1: Build ----------
FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /app

# Copy files
COPY pom.xml .
COPY src ./src

# Build JAR
RUN mvn clean package -DskipTests


# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Render will override)
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
