package com.domain.adapters;

import com.domain.dto.Card;
import com.domain.dto.CardNumber;
import com.domain.ports.repository.CardRepository;
import com.domain.ports.usecase.BatchCreateCardsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private static final int BATCH_SIZE = 1000;
    
    private final CardRepository cardRepository;

    public int execute(InputStream inputStream) {
        int totalProcessed = 0;
        List<Card> batch = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Process lines with exact length of 51 characters
                if (line.length() < 51) {
                    line = String.format("%-51s", line); // Pad with spaces to 51 chars
                }
                
                // Skip header line (first line)
                if (isFirstLine) {
                    isFirstLine = false;
                    log.debug("Skipping header: {}", maskLine(line));
                    continue;
                }
                
                // Skip footer line (starts with "LOTE")
                if (line.startsWith("LOTE")) {
                    log.debug("Skipping footer: {}", maskLine(line));
                    continue;
                }
                
                // Process card lines (start with "C")
                if (line.startsWith("C")) {
                    try {
                        String cardNumber = extractCardNumber(line);
                        if (cardNumber != null && !cardNumber.isEmpty()) {
                            CardNumber validCardNumber = CardNumber.of(cardNumber);
                            Card card = Card.create(validCardNumber);
                            batch.add(card);
                            
                            if (batch.size() >= BATCH_SIZE) {
                                cardRepository.saveAll(batch);
                                totalProcessed += batch.size();
                                log.info("Processed batch of {} cards. Total: {}", batch.size(), totalProcessed);
                                batch.clear();
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Invalid card number on line: {}", maskLine(line), e);
                    }
                }
            }
            
            if (!batch.isEmpty()) {
                cardRepository.saveAll(batch);
                totalProcessed += batch.size();
                log.info("Processed final batch of {} cards. Total: {}", batch.size(), totalProcessed);
            }
            
        } catch (Exception e) {
            log.error("Error processing batch file", e);
            throw new RuntimeException("Error processing batch file", e);
        }

        return totalProcessed;
    }
    
    private String extractCardNumber(String line) {
        if (line.length() < 26) return null;
        
        // Extract card number from position 8-26 (0-based: 7-25)
        String cardNumber = line.substring(7, Math.min(26, line.length())).trim();
        
        // Validate card number format (must be 13-19 digits)
        if (cardNumber.matches("\\d{13,19}")) {
            return cardNumber;
        }
        
        return null;
    }
    
    private String maskLine(String line) {
        if (line.length() < 26) return line;
        return line.substring(0, 7) + "*".repeat(Math.min(19, line.length() - 7));
    }
}