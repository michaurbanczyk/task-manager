package com.example.taskmanager.service;

import com.example.taskmanager.dto.request.UpdateUserRequest;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public void deleteUser(UUID id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }


    @Transactional
    public User updateUser(UUID id, UpdateUserRequest request) {
        User user = getUserById(id);
        if (request.username() != null) {
            String updatedUsername = request.username();
            throwExceptionIfUserExists(updatedUsername);
            user.setUsername(updatedUsername);
        }
        if (request.email() != null) {
            String updatedEmail = request.email();
            user.setEmail(updatedEmail);
        }

        return userRepository.save(user);
    }

    private void throwExceptionIfUserExists(String username) {
        Optional<User> possibleUser = userRepository.findByUsername(username);
        if (possibleUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
    }
}
