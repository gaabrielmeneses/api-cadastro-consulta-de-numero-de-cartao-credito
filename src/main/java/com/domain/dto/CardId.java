package com.domain.dto;

import java.util.Objects;
import java.util.UUID;

public final class CardId extends ValueObject {
    private final UUID value;

    private CardId(UUID value) {
        this.value = Objects.requireNonNull(value, "Card ID cannot be null");
    }

    public static CardId generate() {
        return new CardId(UUID.randomUUID());
    }

    public static CardId of(UUID value) {
        return new CardId(value);
    }

    public static CardId of(String value) {
        try {
            return new CardId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + value, e);
        }
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CardId cardId = (CardId) obj;
        return Objects.equals(value, cardId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}