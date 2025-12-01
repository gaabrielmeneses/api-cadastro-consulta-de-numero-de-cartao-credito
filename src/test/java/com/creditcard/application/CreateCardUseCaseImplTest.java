package com.creditcard.application;

import com.domain.adapters.CreateCardUseCaseImpl;
import com.domain.dto.Card;
import com.domain.ports.repository.CardRepository;
import com.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCardUseCaseImplTest {

    @Mock
    private CardRepository cardRepository;

    private CreateCardUseCaseImpl createCardUseCaseImpl;

    @BeforeEach
    void setUp() {
        createCardUseCaseImpl = new CreateCardUseCaseImpl(cardRepository);
    }

    @Test
    void shouldCreateCardSuccessfully() {
        String validCardNumber = "4532015112830366";
        Card expectedCard = Card.create(CardNumber.of(validCardNumber));
        
        when(cardRepository.save(any(Card.class))).thenReturn(expectedCard);
        
        Card result = createCardUseCaseImpl.execute(validCardNumber);
        
        assertNotNull(result);
        assertEquals(validCardNumber, result.getCardNumber().getValue());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void shouldThrowExceptionForInvalidCardNumber() {
        String invalidCardNumber = "invalid";
        
        assertThrows(DomainException.class, 
            () -> createCardUseCaseImpl.execute(invalidCardNumber));
        
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void shouldThrowExceptionForNullCardNumber() {
        assertThrows(DomainException.class, 
            () -> createCardUseCaseImpl.execute(null));
        
        verify(cardRepository, never()).save(any(Card.class));
    }
}