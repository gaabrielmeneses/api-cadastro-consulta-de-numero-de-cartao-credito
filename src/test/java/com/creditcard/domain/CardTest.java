package com.creditcard.domain;

import com.domain.dto.Card;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void shouldCreateNewCard() {
        CardNumber cardNumber = CardNumber.of("4532015112830366");
        Card card = Card.create(cardNumber);
        
        assertNotNull(card.getId());
        assertEquals(cardNumber, card.getCardNumber());
        assertNotNull(card.getCreatedAt());
    }

    @Test
    void shouldRestoreExistingCard() {
        CardId cardId = CardId.generate();
        CardNumber cardNumber = CardNumber.of("4532015112830366");
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        
        Card card = Card.restore(cardId, cardNumber, createdAt);
        
        assertEquals(cardId, card.getId());
        assertEquals(cardNumber, card.getCardNumber());
        assertEquals(createdAt, card.getCreatedAt());
    }

    @Test
    void shouldRejectNullCardId() {
        CardNumber cardNumber = CardNumber.of("4532015112830366");
        LocalDateTime createdAt = LocalDateTime.now();
        
        assertThrows(NullPointerException.class, 
            () -> Card.restore(null, cardNumber, createdAt));
    }

    @Test
    void shouldRejectNullCardNumber() {
        CardId cardId = CardId.generate();
        LocalDateTime createdAt = LocalDateTime.now();
        
        assertThrows(NullPointerException.class, 
            () -> Card.restore(cardId, null, createdAt));
    }

    @Test
    void shouldRejectNullCreatedAt() {
        CardId cardId = CardId.generate();
        CardNumber cardNumber = CardNumber.of("4532015112830366");
        
        assertThrows(NullPointerException.class, 
            () -> Card.restore(cardId, cardNumber, null));
    }

    @Test
    void shouldImplementEqualsBasedOnId() {
        CardNumber cardNumber = CardNumber.of("4532015112830366");
        Card card1 = Card.create(cardNumber);
        Card card2 = Card.create(cardNumber);
        
        assertNotEquals(card1, card2); // Different IDs
        assertEquals(card1, card1); // Same instance
    }

    @Test
    void shouldImplementHashCodeBasedOnId() {
        CardNumber cardNumber = CardNumber.of("4532015112830366");
        Card card = Card.create(cardNumber);
        
        assertEquals(card.getId().hashCode(), card.hashCode());
    }
}