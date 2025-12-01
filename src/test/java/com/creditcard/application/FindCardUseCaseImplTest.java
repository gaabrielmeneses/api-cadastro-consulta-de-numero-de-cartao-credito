package com.creditcard.application;

import com.domain.adapters.FindCardUseCaseImpl;
import com.domain.dto.Card;
import com.domain.exceptions.CardNotFoundException;
import com.domain.ports.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCardUseCaseImplTest {

    @Mock
    private CardRepository cardRepository;

    private FindCardUseCaseImpl findCardUseCaseImpl;

    @BeforeEach
    void setUp() {
        findCardUseCaseImpl = new FindCardUseCaseImpl(cardRepository);
    }

    @Test
    void shouldFindCardSuccessfully() {
        String cardNumber = "4532015112830366";
        Card expectedCard = Card.create(CardNumber.of(cardNumber));
        
        when(cardRepository.findByCardNumber(any(CardNumber.class)))
            .thenReturn(Optional.of(expectedCard));
        
        Card result = findCardUseCaseImpl.execute(cardNumber);
        
        assertNotNull(result);
        assertEquals(cardNumber, result.getCardNumber().getValue());
        verify(cardRepository).findByCardNumber(any(CardNumber.class));
    }

    @Test
    void shouldThrowExceptionWhenCardNotFound() {
        String cardNumber = "4532015112830366";
        
        when(cardRepository.findByCardNumber(any(CardNumber.class)))
            .thenReturn(Optional.empty());
        
        assertThrows(CardNotFoundException.class, 
            () -> findCardUseCaseImpl.execute(cardNumber));
        
        verify(cardRepository).findByCardNumber(any(CardNumber.class));
    }
}