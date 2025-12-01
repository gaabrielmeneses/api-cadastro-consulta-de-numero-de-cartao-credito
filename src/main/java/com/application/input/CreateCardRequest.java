package com.application.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record CreateCardRequest(

        @NotBlank(message = "Card number is required.")
        @Pattern(
                regexp = "\\d{13,19}",
                message = "Card number must contain between 13 and 19 digits."
        )
        String cardNumber

) {}
