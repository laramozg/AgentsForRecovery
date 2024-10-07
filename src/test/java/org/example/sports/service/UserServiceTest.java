package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.example.sports.model.User;
import org.example.sports.model.enums.Role;
import org.example.sports.repositore.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserServiceTest extends AbstractServiceTest{

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        buildCreateUser();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private void buildCreateUser() {
        userRepository.save(User.builder().username("john").nick("John").telegram("@john").role(Role.EXECUTOR).build());
    }

    @Test
    void createUserSuccessfully() {
        CreateUserRequest createUserRequest = new CreateUserRequest("user1", "user1", "@user1", "USER_PASSWORD", "EXECUTOR");
        User createdUser = userService.createUser(createUserRequest);

        assertNotNull(createdUser);
        assertEquals("user1", createdUser.getUsername());
        assertEquals("user1", createdUser.getNick());
        assertEquals("@user1", createdUser.getTelegram());
    }

    @Test
    void getUserSuccessfully() {
        User fetchedUser = userService.getUserById("john");

        assertNotNull(fetchedUser);
        assertEquals("john", fetchedUser.getUsername());
    }

    @Test
    void deleteUserSuccessfully() {
        userService.deleteUser("john");

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById("john"));
    }

    @Test
    void updateUserSuccessfully() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Alice Updated", "alice_telegram_updated");

        User updatedUser = userService.updateUser("john", updateUserRequest);

        assertEquals("Alice Updated", updatedUser.getNick());
        assertEquals("alice_telegram_updated", updatedUser.getTelegram());
    }

    @Test
    void throwExceptionWhenUsernameAlreadyExists() {
        CreateUserRequest createUserRequest = new CreateUserRequest("john", "John", "@john", "USER_PASSWORD", "EXECUTOR");
        userService.createUser(createUserRequest);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(createUserRequest);
        });

        assertEquals("This username john is already in use.", exception.getMessage());
    }

    @Test
    void throwExceptionWhenUserNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById("nonExistentUser");
        });

        assertEquals("User with username 'nonExistentUser' not found", exception.getMessage());
    }

    @Test
    void throwExceptionWhenDeletingNonExistentUser() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser("nonExistentUser");
        });

        assertEquals("User with username 'nonExistentUser' not found", exception.getMessage());
    }
}
