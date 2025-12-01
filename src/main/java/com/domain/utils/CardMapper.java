package com.domain.utils;

import com.domain.dto.Card;
import com.domain.dto.CardId;
import com.domain.dto.CardNumber;
import com.infrastructure.repository.mysql.Entity.CardEntity;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {
    
    public Card toDomain(CardEntity entity, String decryptedCardNumber) {
        CardId cardId = CardId.of(entity.getId());
        CardNumber cardNumber = CardNumber.of(decryptedCardNumber);
        
        return Card.restore(cardId, cardNumber, entity.getCreatedAt());
    }
}