package com.infrastructure.repository.mysql.repository;

import com.domain.dto.Card;
import com.domain.dto.CardNumber;
import com.domain.ports.repository.CardRepository;
import com.domain.ports.usecase.EncryptionUseCase;
import com.domain.utils.CardMapper;
import com.infrastructure.repository.mysql.Entity.CardEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CardRepositoryImpl implements CardRepository {
    private final CardJpaRepository jpaRepository;
    private final CardMapper cardMapper;
    private final EncryptionUseCase encryptionUseCase;

    public CardRepositoryImpl(CardJpaRepository jpaRepository, 
                             CardMapper cardMapper,
                             EncryptionUseCase encryptionUseCase) {
        this.jpaRepository = jpaRepository;
        this.cardMapper = cardMapper;
        this.encryptionUseCase = encryptionUseCase;
    }

    @Override
    public Card save(Card card) {
        String encryptedCardNumber = encryptionUseCase.encrypt(card.getCardNumber().getValue());
        String cardHash = encryptionUseCase.hash(card.getCardNumber().getValue());
        
        CardEntity entity = new CardEntity(
                card.getId().getValue(),
                encryptedCardNumber,
                cardHash
        );
        
        jpaRepository.save(entity);
        return card;
    }

    @Override
    public Optional<Card> findByCardNumber(CardNumber cardNumber) {
        String cardHash = encryptionUseCase.hash(cardNumber.getValue());
        
        return jpaRepository.findByCardHash(cardHash)
                .map(entity -> {
                    String decryptedCardNumber = encryptionUseCase.decrypt(entity.getEncryptedCardNumber());
                    return cardMapper.toDomain(entity, decryptedCardNumber);
                });
    }

    @Override
    public void saveAll(List<Card> cards) {
        List<CardEntity> entities = cards.stream()
                .map(card -> {
                    String encryptedCardNumber = encryptionUseCase.encrypt(card.getCardNumber().getValue());
                    String cardHash = encryptionUseCase.hash(card.getCardNumber().getValue());
                    return new CardEntity(card.getId().getValue(), encryptedCardNumber, cardHash);
                })
                .toList();
        
        jpaRepository.saveAll(entities);
    }
}