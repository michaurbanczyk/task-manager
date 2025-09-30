package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasksForUser(@AuthenticationPrincipal User user) {
        return taskService.getTasksForUser(user);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, @AuthenticationPrincipal User user) {
        return taskService.addTask(task, user);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable int taskId, @AuthenticationPrincipal User user) {
        taskService.deleteTask(taskId, user);
    }
}
