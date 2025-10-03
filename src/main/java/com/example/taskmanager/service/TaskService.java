package com.example.taskmanager.service;

import com.example.taskmanager.dto.task.CreateTaskRequest;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByUser(user);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(CreateTaskRequest request) {
        UUID userId = request.userId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(String.format("User with ID: %s not found", userId)));
        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .user(user).build();

        return taskRepository.save(task);
    }

    public void deleteTask(long taskId, User user) throws IllegalArgumentException {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (!task.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("You can only delete your own tasks");
        }

        taskRepository.delete(task);
    }

}
