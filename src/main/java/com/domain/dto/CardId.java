package com.domain.dto;

import java.util.UUID;

public final class CardId extends ValueObject<UUID> {
    
    public CardId(UUID value) {
        super(value);
    }

    public static CardId generate() {
        return new CardId(UUID.randomUUID());
    }

    public static CardId of(UUID value) {
        return new CardId(value);
    }
}