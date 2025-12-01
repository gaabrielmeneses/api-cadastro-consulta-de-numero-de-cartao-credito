package com.infrastructure.configuration.auth;

import com.domain.dto.User;

public interface JwtTokenService {
    String generateToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token, String username);
}