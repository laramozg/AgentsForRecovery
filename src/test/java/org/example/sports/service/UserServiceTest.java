package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.example.sports.controller.user.dto.UserDto;
import org.example.sports.repositore.AuthorizationRepository;
import org.example.sports.repositore.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final CreateUserRequest createUserRequest = new CreateUserRequest("john", "John", "@john", "USER_PASSWORD", "EXECUTOR");

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        authorizationRepository.deleteAll();
    }

    @Test
    void testCreateUser() {
        UserDto createdUser = userService.createUser(createUserRequest);

        assertNotNull(createdUser);
        assertEquals("john", createdUser.username());
        assertEquals("John", createdUser.nick());
        assertEquals("@john", createdUser.telegram());
    }

    @Test
    void testGetUser() {
        userService.createUser(createUserRequest);

        UserDto fetchedUser = userService.getUser("john");

        assertNotNull(fetchedUser);
        assertEquals("john", fetchedUser.username());
    }

    @Test
    void testDeleteUser() {
        userService.createUser(createUserRequest);

        userService.deleteUser("john");

        assertThrows(EntityNotFoundException.class, () -> userService.getUser("john"));
    }

    @Test
    void testUpdateUser() {
        userService.createUser(createUserRequest);
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Alice Updated", "alice_telegram_updated");

        UserDto updatedUser = userService.updateUser("john", updateUserRequest);

        assertEquals("Alice Updated", updatedUser.nick());
        assertEquals("alice_telegram_updated", updatedUser.telegram());
    }
}
