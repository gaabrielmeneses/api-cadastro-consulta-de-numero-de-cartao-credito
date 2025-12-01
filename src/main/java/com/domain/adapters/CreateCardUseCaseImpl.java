package com.domain.adapters;

import com.domain.ports.repository.CardRepository;
import com.domain.dto.Card;
import com.domain.dto.CardNumber;

public class CreateCardUseCaseImpl {
    private final CardRepository cardRepository;

    public CreateCardUseCaseImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card execute(String cardNumber) {
        CardNumber validCardNumber = CardNumber.of(cardNumber);
        Card card = Card.create(validCardNumber);
        return cardRepository.save(card);
    }
}