FROM eclipse-temurin:17-jre-jammy

# Create non-root user (For security)
RUN useradd -m -u 1000 appuser

# Set working directory inside container
WORKDIR /app

# Copy the fat jar built by Maven
COPY target/maxx-dental-backend-*.jar maxx-dental-backend.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run as non-root user
USER appuser

# Entrypoint — pass JVM options via environment variable if needed
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/maxx-dental-backend.jar" ]