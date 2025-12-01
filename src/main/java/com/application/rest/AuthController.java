package com.application.rest;

import com.application.input.LoginRequest;
import com.application.output.LoginResponse;
import com.domain.adapters.AuthenticationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication operations")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationUseCase authenticationUseCase;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates user and returns JWT token")
    public ResponseEntity<LoginResponse> log(@Valid @RequestBody LoginRequest request) {
        MDC.put("operation", "login");
        MDC.put("username", request.username());

        log.info("User authentication attempt");

        String token = authenticationUseCase.execute(request.username(), request.password());
        LoginResponse response = new LoginResponse(token);

        log.info("User authenticated successfully");

        return ResponseEntity.ok(response);
    }
}