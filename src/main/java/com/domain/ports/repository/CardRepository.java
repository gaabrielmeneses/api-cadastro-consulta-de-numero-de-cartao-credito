package com.domain.ports.repository;

import com.domain.dto.Card;
import com.domain.dto.CardNumber;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    Card save(Card card);
    Optional<Card> findByCardNumber(CardNumber cardNumber);
    void saveAll(List<Card> cards);
}