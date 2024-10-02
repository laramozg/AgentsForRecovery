package org.example.sports.model.dto;

public record CreateUserRequest(
        String username,
        String nick,
        String telegram,
        String password,
        String  role
) {}