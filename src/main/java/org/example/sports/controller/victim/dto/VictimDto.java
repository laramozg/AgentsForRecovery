package org.example.sports.controller.victim.dto;

public record VictimDto(
        Long id,
        String firstName,
        String lastName,
        String workplace,
        String position,
        String residence,
        String phone,
        String description
) {
}
