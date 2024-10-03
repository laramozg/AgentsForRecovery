package org.example.sports.controller.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateOrderRequest(
        @NotBlank String username,
        @NotNull Long cityId,
        @NotNull Long victimId,
        @NotNull LocalDate deadline,
        List<Long> mutilationIds

) {
}
