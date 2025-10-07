package com.example.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}