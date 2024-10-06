package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.example.sports.controller.user.dto.UserDto;
import org.example.sports.model.User;
import org.example.sports.model.enums.Role;
import org.example.sports.repositore.UserRepository;
import org.junit.jupiter.api.*;
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
        UserDto createdUser = userService.createUser(createUserRequest);

        assertNotNull(createdUser);
        assertEquals("user1", createdUser.username());
        assertEquals("user1", createdUser.nick());
        assertEquals("@user1", createdUser.telegram());
    }

    @Test
    void getUserSuccessfully() {
        UserDto fetchedUser = userService.getUser("john");

        assertNotNull(fetchedUser);
        assertEquals("john", fetchedUser.username());
    }

    @Test
    void deleteUserSuccessfully() {
        userService.deleteUser("john");

        assertThrows(EntityNotFoundException.class, () -> userService.getUser("john"));
    }

    @Test
    void updateUserSuccessfully() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Alice Updated", "alice_telegram_updated");

        UserDto updatedUser = userService.updateUser("john", updateUserRequest);

        assertEquals("Alice Updated", updatedUser.nick());
        assertEquals("alice_telegram_updated", updatedUser.telegram());
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
            userService.getUser("nonExistentUser");
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
