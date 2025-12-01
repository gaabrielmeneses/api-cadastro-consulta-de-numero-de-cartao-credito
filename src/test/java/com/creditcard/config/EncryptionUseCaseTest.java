package com.creditcard.config;

import com.domain.adapters.EncryptionUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUseCaseTest {

    private EncryptionUseCaseImpl encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionUseCaseImpl("testEncryptionKey1234567890123456");
    }

    @Test
    void shouldEncryptAndDecryptSuccessfully() {
        String plainText = "4532015112830366";
        
        String encrypted = encryptionService.encrypt(plainText);
        String decrypted = encryptionService.decrypt(encrypted);
        
        assertNotEquals(plainText, encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    void shouldGenerateConsistentHash() {
        String text = "4532015112830366";
        
        String hash1 = encryptionService.hash(text);
        String hash2 = encryptionService.hash(text);
        
        assertEquals(hash1, hash2);
        assertNotEquals(text, hash1);
    }

    @Test
    void shouldGenerateDifferentHashesForDifferentInputs() {
        String text1 = "4532015112830366";
        String text2 = "5555555555554444";
        
        String hash1 = encryptionService.hash(text1);
        String hash2 = encryptionService.hash(text2);
        
        assertNotEquals(hash1, hash2);
    }

    @Test
    void shouldThrowExceptionForInvalidEncryptedData() {
        assertThrows(RuntimeException.class, 
            () -> encryptionService.decrypt("invalid-encrypted-data"));
    }
}