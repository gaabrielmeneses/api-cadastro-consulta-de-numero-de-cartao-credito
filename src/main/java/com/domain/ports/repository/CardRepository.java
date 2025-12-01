package com.domain.ports.repository;

import com.domain.dto.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    Card save(Card card);
    Optional<Card> findByCardNumber(String cardNumber);
    void saveAll(List<Card> cards);
}