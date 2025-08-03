# Use official OpenJDK 17 base image
FROM eclipse-temurin:17-jdk-jammy

# Argument for the JAR file path (relative to build context)
ARG JAR_FILE=target/theatre-0.0.1-SNAPSHOT.jar

# Create app directory inside container
WORKDIR /app

# Copy the Spring Boot fat JAR into container
COPY ${JAR_FILE} app.jar

# Expose application port
EXPOSE 7200

# Run the jar file as a non-root user for security best practices
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

# Command to run your Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
