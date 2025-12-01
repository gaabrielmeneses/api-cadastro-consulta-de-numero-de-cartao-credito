package com.application.input;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Arrays;

/**
 * Secure value object for passwords.
 * Uses char[] instead of String to allow clearing from memory and prevent retention in string pool.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {

    char[] value;

    /**
     * Creates a defensive copy of the password to prevent external mutation.
     */
    public static Password from(char[] password) {
        if (password == null || password.length == 0) {
            throw new IllegalArgumentException("Password is required");
        }
        char[] copy = new char[password.length];
        System.arraycopy(password, 0, copy, 0, password.length);
        return new Password(copy);
    }

    /**
     * Clears the password from memory when no longer needed.
     * Should be called in a finally block after authentication.
     */
    public void clear() {
        Arrays.fill(value, '\0');
    }

    /**
     * Prevents accidental logging of the password.
     */
    @Override
    public String toString() {
        return "Password[REDACTED]";
    }
}
