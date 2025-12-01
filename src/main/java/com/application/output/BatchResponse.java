package com.application.output;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for batch card creation")
public record BatchResponse(
    @Schema(description = "Number of cards processed", example = "1500")
    int cardsProcessed
) {}