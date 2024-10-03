package org.example.sports.controller.fight.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFight(
        @NotBlank String executorId,
        @NotNull Long orderId
) {
}
