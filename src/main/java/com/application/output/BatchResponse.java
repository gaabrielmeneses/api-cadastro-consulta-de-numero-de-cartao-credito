package com.application.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class  BatchResponse {
    @Schema(description = "Number of cards processed", example = "1500")
    int cardsProcessed;
}