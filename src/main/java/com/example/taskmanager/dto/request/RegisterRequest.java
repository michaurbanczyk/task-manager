package com.example.taskmanager.dto.request;

import com.example.taskmanager.model.Role;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        String username,
        @NotBlank
        String email,
        @NotBlank
        String password,
        Role role
) {
}
