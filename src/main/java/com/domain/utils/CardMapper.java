package com.domain.utils;

import com.domain.dto.Card;
import com.domain.dto.CardId;
import com.domain.dto.CardNumber;
import com.infrastructure.repository.mysql.Entity.CardEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CardMapper {
    
    public Card toDomain(CardEntity entity, String decryptedCardNumber) {
        return new Card(
            CardId.of(entity.getId()),
            CardNumber.of(decryptedCardNumber),
            LocalDateTime.now()
        );
    }
}