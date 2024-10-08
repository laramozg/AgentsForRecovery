package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.example.sports.model.AuthorizationData;
import org.example.sports.model.User;
import org.example.sports.model.enums.Role;
import org.example.sports.repositore.AuthorizationRepository;
import org.example.sports.repositore.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.example.sports.util.Models.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorizationRepository authorizationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldCreateUser_WhenValidRequest() {
        CreateUserRequest createUserRequest = CREATE_USER_REQUEST();

        when(passwordEncoder.encode(createUserRequest.password())).thenReturn(ENCODED_USER_PASSWORD);
        when(authorizationRepository.findById(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(USER_EXECUTOR());
        when(authorizationRepository.save(any(AuthorizationData.class))).thenReturn(AUTH_DATA());

        User createdUser = userService.createUser(createUserRequest);

        assertNotNull(createdUser);
        assertEquals(createUserRequest.username(), createdUser.getUsername());
        assertEquals(createUserRequest.nick(), createdUser.getNick());
        assertEquals(createUserRequest.telegram(), createdUser.getTelegram());
        assertEquals(Role.EXECUTOR, createdUser.getRole());
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameAlreadyExists() {
        when(authorizationRepository.findById(anyString())).thenReturn(Optional.of(AUTH_DATA()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(CREATE_USER_REQUEST());
        });

        assertEquals("This username john.doe is already in use.", exception.getMessage());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        User user = USER_EXECUTOR();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        User result = userService.getUserById(user.getUsername());

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById("unknownUser");
        });

        assertEquals("User with username 'unknownUser' not found", exception.getMessage());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.existsById(anyString())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyString());

        userService.deleteUser("john.doe");

        verify(userRepository, times(1)).existsById("john.doe");
        verify(userRepository, times(1)).deleteById("john.doe");
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsById(anyString())).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser("unknownUser");
        });

        assertEquals("User with username 'unknownUser' not found", exception.getMessage());
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() {
        User user = USER_EXECUTOR();
        UpdateUserRequest updateUserRequest = UPDATE_USER_REQUEST();

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser("john.doe", updateUserRequest);

        assertNotNull(updatedUser);
        assertEquals(updateUserRequest.nick(), updatedUser.getNick());
        assertEquals(updateUserRequest.telegram(), updatedUser.getTelegram());
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser("unknownUser", UPDATE_USER_REQUEST());
        });

        assertEquals("User with username 'unknownUser' not found", exception.getMessage());
    }
}
