package com.domain.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Card {
    private final CardId id;
    private final CardNumber cardNumber;
    private final LocalDateTime createdAt;

    private Card(CardId id, CardNumber cardNumber, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "Card ID cannot be null");
        this.cardNumber = Objects.requireNonNull(cardNumber, "Card number cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
    }

    public static Card create(CardNumber cardNumber) {
        return new Card(CardId.generate(), cardNumber, LocalDateTime.now());
    }

    public static Card restore(CardId id, CardNumber cardNumber, LocalDateTime createdAt) {
        return new Card(id, cardNumber, createdAt);
    }

    public CardId getId() {
        return id;
    }

    public CardNumber getCardNumber() {
        return cardNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber=" + cardNumber +
                ", createdAt=" + createdAt +
                '}';
    }
}