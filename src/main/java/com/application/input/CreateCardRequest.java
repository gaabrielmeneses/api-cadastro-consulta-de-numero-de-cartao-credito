package com.application.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardRequest {

        @NotBlank(message = "Card number is required.")
        @Pattern(
                regexp = "\\d{13,19}",
                message = "Card number must contain between 13 and 19 digits."
        )
        String cardNumber;

}
