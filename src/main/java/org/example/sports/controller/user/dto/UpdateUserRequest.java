package org.example.sports.controller.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(@NotBlank String nick,
                                @NotBlank String telegram) {
}
