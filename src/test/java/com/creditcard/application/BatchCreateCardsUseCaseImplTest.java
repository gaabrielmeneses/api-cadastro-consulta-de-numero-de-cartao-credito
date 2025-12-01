package com.creditcard.application;

import com.domain.adapters.BatchCreateCardsUseCaseImpl;
import com.domain.dto.Card;
import com.domain.ports.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchCreateCardsUseCaseImplTest {

    @Mock
    private CardRepository cardRepository;

    private BatchCreateCardsUseCaseImpl batchCreateCardsUseCaseImpl;

    @BeforeEach
    void setUp() {
        batchCreateCardsUseCaseImpl = new BatchCreateCardsUseCaseImpl(cardRepository);
    }

    @Test
    void shouldProcessStructuredFileFormat() {
        String fileContent = """
            DESAFIO-HYPERATIVA           20180524LOTE0001000003   
            C1     4532015112830366                               
            C2     5555555555554444                               
            C3     378282246310005                                
            LOTE0001000003                                        
            """;
        
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        
        int result = batchCreateCardsUseCaseImpl.execute(inputStream);
        
        assertEquals(3, result);
        
        ArgumentCaptor<List<Card>> captor = ArgumentCaptor.forClass(List.class);
        verify(cardRepository).saveAll(captor.capture());
        
        List<Card> savedCards = captor.getValue();
        assertEquals(3, savedCards.size());
        assertEquals("4532015112830366", savedCards.get(0).getCardNumber().getValue());
        assertEquals("5555555555554444", savedCards.get(1).getCardNumber().getValue());
        assertEquals("378282246310005", savedCards.get(2).getCardNumber().getValue());
    }

    @Test
    void shouldSkipInvalidLines() {
        String fileContent = """
            DESAFIO-HYPERATIVA           20180524LOTE0001000002   
            C1     4532015112830366                               
            C2     invalid-card-number                            
            C3     5555555555554444                               
            LOTE0001000002                                        
            """;
        
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        
        int result = batchCreateCardsUseCaseImpl.execute(inputStream);
        
        assertEquals(2, result);
        verify(cardRepository).saveAll(anyList());
    }

    @Test
    void shouldHandleEmptyFile() {
        String fileContent = """
            DESAFIO-HYPERATIVA           20180524LOTE0001000000   
            LOTE0001000000                                        
            """;
        
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        
        int result = batchCreateCardsUseCaseImpl.execute(inputStream);
        
        assertEquals(0, result);
        verify(cardRepository, never()).saveAll(anyList());
    }
}