package com.domain.exceptions;

public class CardNotFoundException extends DomainException {
    public CardNotFoundException(String cardNumber) {
        super("Card not found with number: " + maskCardNumber(cardNumber));
    }

    private static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() <= 4) {
            return cardNumber;
        }
        return "*".repeat(cardNumber.length() - 4) + cardNumber.substring(cardNumber.length() - 4);
    }
}