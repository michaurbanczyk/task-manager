package com.example.taskmanager.dto.request;

import java.time.Instant;

public record UpdateTaskRequest(
        String title,
        String description,
        Instant dueDate
) {
}
