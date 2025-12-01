package com.domain.adapters;

import com.domain.dto.Card;
import com.domain.ports.repository.CardRepository;
import com.domain.ports.usecase.BatchCreateCardsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchCreateCardsUseCaseImpl implements BatchCreateCardsUseCase {
    private static final Logger logger = LoggerFactory.getLogger(BatchCreateCardsUseCaseImpl.class);
    private static final int BATCH_SIZE = 1000;
    
    private final CardRepository cardRepository;

    public int execute(InputStream inputStream) {
        int totalProcessed = 0;
        List<Card> batch = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                
                if (trimmedLine.isEmpty()) continue;
                
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip footer line (starts with "LOTE")
                if (trimmedLine.startsWith("LOTE")) {
                    continue;
                }
                
                // Process card lines (start with "C")
                if (trimmedLine.startsWith("C")) {
                    try {
                        String cardNumber = extractCardNumber(trimmedLine);
                        if (cardNumber != null) {
                            CardNumber validCardNumber = CardNumber.of(cardNumber);
                            Card card = Card.create(validCardNumber);
                            batch.add(card);
                            
                            if (batch.size() >= BATCH_SIZE) {
                                cardRepository.saveAll(batch);
                                totalProcessed += batch.size();
                                logger.info("Processed batch of {} cards. Total: {}", batch.size(), totalProcessed);
                                batch.clear();
                            }
                        }
                    } catch (Exception e) {
                        logger.warn("Invalid card number on line: {}", trimmedLine, e);
                    }
                }
            }
            
            if (!batch.isEmpty()) {
                cardRepository.saveAll(batch);
                totalProcessed += batch.size();
                logger.info("Processed final batch of {} cards. Total: {}", batch.size(), totalProcessed);
            }
            
        } catch (Exception e) {
            logger.error("Error processing batch file", e);
            throw new RuntimeException("Error processing batch file", e);
        }

        return totalProcessed;
    }
    
    private String extractCardNumber(String line) {
        if (line.length() < 26) return null;
        
        // Extract card number from position 8-26 (0-based: 7-25)
        String cardNumber = line.substring(7, Math.min(26, line.length())).trim();
        
        return cardNumber.isEmpty() ? null : cardNumber;
    }
}