package com.domain.adapters;

import com.domain.dto.Card;
import com.domain.ports.repository.CardRepository;
import com.domain.ports.usecase.CreateCardUseCase;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@AllArgsConstructor
public class CreateCardUseCaseImpl implements CreateCardUseCase {

    private final CardRepository cardRepository;

    public Card execute(String cardNumber) {
        CardNumber validCardNumber = CardNumber.of(cardNumber);
        Card card = Card.create(validCardNumber);
        return cardRepository.save(card);
    }
}