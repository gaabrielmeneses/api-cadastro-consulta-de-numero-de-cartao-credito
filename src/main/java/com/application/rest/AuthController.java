package com.application.rest;

import com.application.input.LoginRequest;
import com.application.output.LoginResponse;
import com.infrastructure.configuration.auth.AuthenticationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final AuthenticationUseCase authenticationUseCase;

    public AuthController(AuthenticationUseCase authenticationUseCase) {
        this.authenticationUseCase = authenticationUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates user and returns JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        MDC.put("operation", "login");
        MDC.put("username", request.username());
        
        logger.info("User authentication attempt");
        
        String token = authenticationUseCase.execute(request.username(), request.password());
        LoginResponse response = new LoginResponse(token);
        
        logger.info("User authenticated successfully");
        
        return ResponseEntity.ok(response);
    }
}