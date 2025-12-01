package com.domain.ports.usecase;

public interface EncryptionUseCase {

    String encrypt(String plainText);
    String decrypt(String encryptedText);
    String hash(String text);
}