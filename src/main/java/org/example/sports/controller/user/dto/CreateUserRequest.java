package org.example.sports.controller.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank String username,
        @NotBlank String nick,
        @NotBlank String telegram,
        @Size(min = 10) String password,
        @NotBlank String role
) {}