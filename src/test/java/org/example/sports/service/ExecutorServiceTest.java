package org.example.sports.service;

import jakarta.transaction.Transactional;
import org.example.sports.controller.executor.dto.ExecutorDto;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.model.Executor;
import org.example.sports.model.User;
import org.example.sports.repositore.ExecutorRepository;
import org.example.sports.repositore.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ExecutorServiceTest {

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
    private ExecutorService executorService;

    @Autowired
    private ExecutorRepository executorRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        executorRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateExecutor() {
        createUser();

        ExecutorRequest executorRequest = new ExecutorRequest("john", "123456", 75.0, 180.0);

        ExecutorDto createdExecutor = executorService.createExecutor(executorRequest);

        assertNotNull(createdExecutor);
        assertEquals("john", createdExecutor.username());
        assertEquals("123456", createdExecutor.passportSeriesNumber());
        assertEquals(75.0, createdExecutor.weight());
        assertEquals(180.0, createdExecutor.height());
        assertEquals(0.0, createdExecutor.rating());
        assertEquals(0, createdExecutor.completedOrders());
    }

    @Test
    void testGetExecutor() {
        User user = createUser();

        createExecutor(user);

        ExecutorDto fetchedExecutor = executorService.getUser("john");

        assertNotNull(fetchedExecutor);
        assertEquals("john", fetchedExecutor.username());
        assertEquals("123456", fetchedExecutor.passportSeriesNumber());
    }

    @Test
    void testUpdateExecutor() {
        User user = createUser();

        createExecutor(user);

        ExecutorRequest updateRequest = new ExecutorRequest("john", "654321", 80.0, 185.0);

        ExecutorDto updatedExecutor = executorService.updateUser("john", updateRequest);

        assertNotNull(updatedExecutor);
        assertEquals("654321", updatedExecutor.passportSeriesNumber());
        assertEquals(80.0, updatedExecutor.weight());
        assertEquals(185.0, updatedExecutor.height());
    }

    private User createUser(){
        User user = User.builder()
                .username("john")
                .nick("John")
                .telegram("@john")
                .build();
        userRepository.save(user);
        return user;
    }

    private void createExecutor(User user){
        Executor executor = Executor.builder()
                .username("john")
                .passportSeriesNumber("123456")
                .weight(75.0)
                .height(180.0)
                .rating(5.0)
                .completedOrders(10)
                .user(user)
                .build();
        executorRepository.save(executor);
    }
}
