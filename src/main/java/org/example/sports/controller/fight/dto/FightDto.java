package org.example.sports.controller.fight.dto;

public record FightDto(
        Long id,
        String executorId,
        Long orderId,
        String status
) {
}
