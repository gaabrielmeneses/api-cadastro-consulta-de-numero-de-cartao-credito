package com.application.rest;

import com.application.input.CreateCardRequest;
import com.application.output.BatchResponse;
import com.application.output.CardResponse;
import com.domain.dto.Card;
import com.domain.ports.usecase.BatchCreateCardsUseCase;
import com.domain.ports.usecase.CreateCardUseCase;
import com.domain.ports.usecase.FindCardUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/cards")
@Tag(name = "Cards", description = "Credit card management operations")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class CardController {
    private static final Logger logger = LoggerFactory.getLogger(CardController.class);
    
    private final CreateCardUseCase createCardUseCase;
    private final FindCardUseCase findCardUseCase;
    private final BatchCreateCardsUseCase batchCreateCardsUseCase;

    @PostMapping
    @Operation(summary = "Create a new credit card", description = "Creates a new credit card with validation")
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CreateCardRequest request) {
        MDC.put("operation", "createCard");
        MDC.put("cardNumber", maskCardNumber(request.cardNumber()));
        
        logger.info("Creating new card");
        
        Card card = createCardUseCase.execute(request.cardNumber());
        CardResponse response = new CardResponse(card.getId().getValue());
        
        logger.info("Card created successfully with ID: {}", card.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{cardNumber}")
    @Operation(summary = "Find card by number", description = "Retrieves card information by card number")
    public ResponseEntity<CardResponse> findCard(@PathVariable String cardNumber) {
        MDC.put("operation", "findCard");
        MDC.put("cardNumber", maskCardNumber(cardNumber));
        
        logger.info("Finding card");
        
        Card card = findCardUseCase.execute(cardNumber);
        CardResponse response = new CardResponse(card.getId().getValue());
        
        logger.info("Card found with ID: {}", card.getId());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    @Operation(summary = "Batch create cards", description = "Creates multiple cards from uploaded text file")
    public ResponseEntity<BatchResponse> batchCreateCards(@RequestParam("file") MultipartFile file) {
        MDC.put("operation", "batchCreateCards");
        MDC.put("fileName", file.getOriginalFilename());
        
        logger.info("Processing batch file with {} bytes", file.getSize());
        
        try {
            int processed = batchCreateCardsUseCase.execute(file.getInputStream());
            BatchResponse response = new BatchResponse(processed);
            
            logger.info("Batch processing completed. Cards processed: {}", processed);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error reading batch file", e);
            throw new RuntimeException("Error reading batch file", e);
        }
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() <= 4) {
            return cardNumber;
        }
        return "*".repeat(cardNumber.length() - 4) + cardNumber.substring(cardNumber.length() - 4);
    }
}