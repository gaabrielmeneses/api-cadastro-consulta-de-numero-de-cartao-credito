package com.infrastructure.configuration.auth;

import com.domain.adapters.AuthenticationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class AuthConfig {

    @Bean
    public AuthenticationUseCase authenticationUseCase(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService) {
        return new AuthenticationUseCase(authenticationManager, jwtTokenService);
    }
}