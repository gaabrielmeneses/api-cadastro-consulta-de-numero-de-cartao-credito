package com.application.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to create a new credit card")
public record CreateCardRequest(
    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{13,19}", message = "Card number must contain 13-19 digits")
    @Schema(description = "Credit card number (13-19 digits)", example = "4532015112830366")
    String cardNumber
) {}