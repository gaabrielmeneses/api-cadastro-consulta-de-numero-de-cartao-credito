package com.domain.dto;

import com.domain.exceptions.DomainException;

import java.util.Objects;

public final class CardNumber extends ValueObject {
    private final String value;

    private CardNumber(String value) {
        this.value = value;
    }

    public static CardNumber of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("Card number cannot be null or empty");
        }
        
        String cleanValue = value.replaceAll("\\s+", "");
        
        if (!isValidLength(cleanValue)) {
            throw new DomainException("Card number must have between 13 and 19 digits");
        }
        
        if (!isNumeric(cleanValue)) {
            throw new DomainException("Card number must contain only digits");
        }
        
        if (!isValidLuhn(cleanValue)) {
            throw new DomainException("Invalid card number (Luhn algorithm failed)");
        }
        
        return new CardNumber(cleanValue);
    }

    private static boolean isValidLength(String value) {
        return value.length() >= 13 && value.length() <= 19;
    }

    private static boolean isNumeric(String value) {
        return value.matches("\\d+");
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

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CardNumber that = (CardNumber) obj;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "CardNumber{" + "value='" + maskValue() + '\'' + '}';
    }

    private String maskValue() {
        if (value.length() <= 4) return value;
        return "*".repeat(value.length() - 4) + value.substring(value.length() - 4);
    }
}