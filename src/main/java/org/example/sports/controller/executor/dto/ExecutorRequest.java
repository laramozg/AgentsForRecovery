package org.example.sports.controller.executor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExecutorRequest(
        @NotBlank String username,
        @NotBlank String passportSeriesNumber,
        @NotNull Double weight,
        @NotNull Double height
) {
}

