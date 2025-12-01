package com.domain.adapters;

import com.domain.dto.Card;
import com.domain.exceptions.CardNotFoundException;
import com.domain.ports.repository.CardRepository;
import com.domain.ports.usecase.FindCardUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindCardUseCaseImpl implements FindCardUseCase {

    private final CardRepository cardRepository;

    public Card execute(String cardNumber) {
        CardNumber validCardNumber = CardNumber.of(cardNumber);
        return cardRepository.findByCardNumber(validCardNumber)
                .orElseThrow(() -> new CardNotFoundException(cardNumber));
    }
}