package com.example.taskmanager.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 5, message = "Username must be at least 5 characters")
        String username,

        @Email(message = "Email is invalid")
        String email
) {
}
