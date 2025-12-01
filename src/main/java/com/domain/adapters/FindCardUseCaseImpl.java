package com.domain.adapters;

import com.domain.exceptions.CardNotFoundException;
import com.domain.ports.repository.CardRepository;
import com.domain.dto.Card;
import com.domain.dto.CardNumber;

public class FindCardUseCaseImpl {
    private final CardRepository cardRepository;

    public FindCardUseCaseImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card execute(String cardNumber) {
        CardNumber validCardNumber = CardNumber.of(cardNumber);
        return cardRepository.findByCardNumber(validCardNumber)
                .orElseThrow(() -> new CardNotFoundException(cardNumber));
    }
}