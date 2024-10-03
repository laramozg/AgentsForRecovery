package org.example.sports.controller.city.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCity(
        @NotBlank String name,
        @NotBlank String region
) {
}
