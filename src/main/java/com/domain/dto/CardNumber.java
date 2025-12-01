package com.domain.dto;

public final class CardNumber extends ValueObject<String> {
    
    public CardNumber(String value) {
        super(validateCardNumber(value));
    }

    public static CardNumber of(String value) {
        return new CardNumber(value);
    }

    private static String validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }
        
        String cleanNumber = cardNumber.replaceAll("\\s+", "");
        
        if (!cleanNumber.matches("\\d{13,19}")) {
            throw new IllegalArgumentException("Card number must contain 13-19 digits");
        }
        
        if (!isValidLuhn(cleanNumber)) {
            throw new IllegalArgumentException("Invalid card number (Luhn check failed)");
        }
        
        return cleanNumber;
    }

    private static boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }
}