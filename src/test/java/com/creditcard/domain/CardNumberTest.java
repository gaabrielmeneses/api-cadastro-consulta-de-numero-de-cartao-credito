package com.creditcard.domain;

import com.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CardNumberTest {

    @Test
    void shouldCreateValidCardNumber() {
        String validCardNumber = "4532015112830366";
        CardNumber cardNumber = CardNumber.of(validCardNumber);
        
        assertEquals(validCardNumber, cardNumber.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "4532015112830366", // Visa
        "5555555555554444", // Mastercard
        "378282246310005",  // American Express
        "6011111111111117"  // Discover
    })
    void shouldAcceptValidCardNumbers(String validNumber) {
        assertDoesNotThrow(() -> CardNumber.of(validNumber));
    }

    @Test
    void shouldRejectNullCardNumber() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> CardNumber.of(null));
        assertEquals("Card number cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldRejectEmptyCardNumber() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> CardNumber.of(""));
        assertEquals("Card number cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldRejectCardNumberWithInvalidLength() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> CardNumber.of("123456789012"));
        assertEquals("Card number must have between 13 and 19 digits", exception.getMessage());
    }

    @Test
    void shouldRejectNonNumericCardNumber() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> CardNumber.of("453201511283036a"));
        assertEquals("Card number must contain only digits", exception.getMessage());
    }

    @Test
    void shouldRejectCardNumberFailingLuhnCheck() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> CardNumber.of("4532015112830367"));
        assertEquals("Invalid card number (Luhn algorithm failed)", exception.getMessage());
    }

    @Test
    void shouldHandleCardNumberWithSpaces() {
        String cardNumberWithSpaces = "4532 0151 1283 0366";
        CardNumber cardNumber = CardNumber.of(cardNumberWithSpaces);
        
        assertEquals("4532015112830366", cardNumber.getValue());
    }

    @Test
    void shouldMaskCardNumberInToString() {
        CardNumber cardNumber = CardNumber.of("4532015112830366");
        String toString = cardNumber.toString();
        
        assertTrue(toString.contains("************0366"));
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        CardNumber cardNumber1 = CardNumber.of("4532015112830366");
        CardNumber cardNumber2 = CardNumber.of("4532015112830366");
        CardNumber cardNumber3 = CardNumber.of("5555555555554444");
        
        assertEquals(cardNumber1, cardNumber2);
        assertNotEquals(cardNumber1, cardNumber3);
        assertEquals(cardNumber1.hashCode(), cardNumber2.hashCode());
    }
}