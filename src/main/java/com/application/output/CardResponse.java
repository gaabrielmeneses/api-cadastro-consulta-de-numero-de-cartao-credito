package com.application.output;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response containing card information")
public record CardResponse(
    @Schema(description = "Unique card identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id
) {}