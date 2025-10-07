package com.example.taskmanager.service;


import com.example.taskmanager.dto.request.UpdateUserRequest;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UUID mockUuid = UUID.randomUUID();
    private final String mockPassword = "password";
    private final String mockUsername = "username";
    private final String mockEmail = "mockEmail";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUserWhenIdExists() {
        // Arrange
        User mockUser = User.builder()
                .id(mockUuid)
                .username(mockUsername)
                .email(mockEmail)
                .password(mockPassword)
                .build();

        when(userRepository.findById(mockUuid)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUserById(mockUuid);

        // Assert
        assertEquals(mockUsername, result.getUsername());
        assertEquals(mockEmail, result.getEmail());
        assertEquals(mockPassword, result.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(mockUuid)).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(mockUuid));

        // Assert
        assertEquals("404 NOT_FOUND \"User not found\"", exception.getMessage());
    }

    @Test
    void shouldReturnUsers() {
        // Arrange
        User mockUser1 = User.builder()
                .id(mockUuid)
                .username(mockUsername)
                .email(mockEmail)
                .password(mockPassword)
                .build();

        User mockUser2 = User.builder()
                .id(mockUuid)
                .username(mockUsername)
                .email(mockEmail)
                .password(mockPassword)
                .build();

        when(userRepository.findAll()).thenReturn(List.of(mockUser1, mockUser2));

        // Act
        List<User> results = userService.getAllUsers();

        // Assert
        assertEquals(results.size(), 2);
    }

    @Test
    void shouldDeleteUser() {
        // Arrange
        User mockUser1 = User.builder()
                .id(mockUuid)
                .username(mockUsername)
                .email(mockEmail)
                .password(mockPassword)
                .build();

        when(userRepository.findById(mockUuid)).thenReturn(Optional.of(mockUser1));

        // Act
        userService.deleteUser(mockUuid);

        // Assert
        verify(userRepository).findById(mockUuid);
        verify(userRepository).delete(mockUser1);
    }

    @Test
    void shouldNotDeleteUserWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(mockUuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            userService.deleteUser(mockUuid);
        });

        verify(userRepository).findById(mockUuid);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void shouldUpdateUser() {
        // Assert
        User mockUser1 = User.builder()
                .id(mockUuid)
                .username(mockUsername)
                .email(mockEmail)
                .password(mockPassword)
                .build();
        when(userRepository.findById(mockUuid)).thenReturn(Optional.of(mockUser1));
        String mockUpdatedUsername = "mockUpdatedUsername";
        String mockUpdatedEmail = "mockUpdatedEmail";

        UpdateUserRequest mockUpdateUserRequest = new UpdateUserRequest(mockUpdatedUsername, mockUpdatedEmail);

        User updatedUser = User.builder()
                .id(mockUuid)
                .username(mockUpdatedUsername)
                .email(mockUpdatedEmail)
                .password(mockPassword)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);


        // Act
        User result = userService.updateUser(mockUuid, mockUpdateUserRequest);

        // Assert
        verify(userRepository).findById(mockUuid);
        verify(userRepository).save(any(User.class));
        assertEquals(mockUpdatedUsername, result.getUsername());
        assertEquals(mockUpdatedEmail, result.getEmail());
    }
}