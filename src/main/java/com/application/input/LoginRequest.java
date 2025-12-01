package com.application.input;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Value // Makes the class immutable, generates getters, proper equals/hashCode (excluding password), and safe toString
@Builder(toBuilder = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotBlank(message = "Username is required")
    String username;

    // Password is encapsulated in a secure value object - never exposed as plain String
    Password password;

    // Private constructor enforces creation only through the factory method (encapsulation)
    private LoginRequest(String username, Password password) {
        this.username = validateUsername(username);
        this.password = Objects.requireNonNull(password, "Password is required");
    }

    /**
     * Factory method (Clean Code pattern).
     * Replaces direct use of @Builder to ensure password is received as char[] and properly wrapped.
     */
    public static LoginRequest of(String username, char[] password) {
        return new LoginRequest(username, Password.from(password));
    }

    /**
     * Explicit validation with clear error message.
     * Follows Single Responsibility Principle and provides fail-fast behavior.
     */
    private static String validateUsername(String username) {
        if (Objects.isNull(username) || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        return username.trim();
    }
}