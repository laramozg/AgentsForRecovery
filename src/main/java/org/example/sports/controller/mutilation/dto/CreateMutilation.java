package org.example.sports.controller.mutilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMutilation (
        @NotBlank String type,
        @NotNull Integer price){
}
