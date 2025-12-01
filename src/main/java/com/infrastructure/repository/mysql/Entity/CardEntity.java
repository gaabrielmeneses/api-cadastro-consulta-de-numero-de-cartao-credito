package com.infrastructure.repository.mysql.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cards", indexes = {
    @Index(name = "idx_card_hash", columnList = "cardHash", unique = true)
})
public class CardEntity {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "encrypted_card_number", nullable = false, length = 500)
    private String encryptedCardNumber;

    @Column(name = "card_hash", nullable = false, length = 64, unique = true)
    private String cardHash;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected CardEntity() {}

    public CardEntity(UUID id, String encryptedCardNumber, String cardHash) {
        this.id = id;
        this.encryptedCardNumber = encryptedCardNumber;
        this.cardHash = cardHash;
    }

    public UUID getId() {
        return id;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public String getCardHash() {
        return cardHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}