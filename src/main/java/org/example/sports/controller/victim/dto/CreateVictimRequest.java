package org.example.sports.controller.victim.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateVictimRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String workplace,
        String position,
        String residence,
        String phone,
        String description
) {
}
