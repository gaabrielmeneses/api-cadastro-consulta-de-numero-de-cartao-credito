FROM amazoncorretto:21-alpine

LABEL maintainer="Credit Card API Team"
LABEL version="1.0.0"
LABEL description="Secure Credit Card Registration and Consultation API"

# Install Maven and curl
RUN apk add --no-cache maven curl

# Create app directory
WORKDIR /app

# Create non-root user
RUN addgroup -S appuser && adduser -S appuser -G appuser

# Copy pom.xml first for better caching
COPY pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Create logs directory
RUN mkdir -p /app/logs && chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "target/api-cadastro-consulta-cartao-1.0.0.jar"]
