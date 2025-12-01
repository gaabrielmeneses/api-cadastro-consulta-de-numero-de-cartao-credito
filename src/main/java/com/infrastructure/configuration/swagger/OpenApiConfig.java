package com.infrastructure.configuration.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Credit Card API",
        version = "1.0.0",
        description = "Secure API for credit card registration and consultation",
        contact = @Contact(
            name = "Credit Card API Support",
            email = "support@creditcardapi.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local server"),
        @Server(url = "https://api.creditcard.com", description = "Production server")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {
}