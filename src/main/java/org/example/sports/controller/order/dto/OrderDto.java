package org.example.sports.controller.order.dto;

import org.example.sports.controller.mutilation.dto.MutilationDto;

import java.time.LocalDate;
import java.util.List;

public record OrderDto(
        Long id,
        String username,
        Long cityId,
        Long victimId,
        LocalDate deadline,
        String state,
        List<MutilationDto> mutilations
) {
}
