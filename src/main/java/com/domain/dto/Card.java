package com.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class Card {

    private CardId id;
    private CardNumber cardNumber;
    private LocalDateTime createdAt;

    public static Card create(CardNumber cardNumber) {
        return new Card(
            CardId.generate(),
            cardNumber,
            LocalDateTime.now()
        );
    }
}