package org.example.sports.service;

import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.model.Executor;
import org.example.sports.model.User;
import org.example.sports.model.enums.Role;
import org.example.sports.repositore.ExecutorRepository;
import org.example.sports.repositore.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ExecutorServiceTest extends AbstractServiceTest {

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private ExecutorRepository executorRepository;

    @Autowired
    private UserRepository userRepository;


    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        executorRepository.deleteAll();
    }

    private User buildCreateUser() {
        return userRepository.save(User.builder().username("john").nick("John").telegram("@john").role(Role.EXECUTOR).build());
    }

    private void buildCreateExecutor(User user) {
        executorRepository.save(Executor.builder().username("john").passportSeriesNumber("123456").weight(75.0)
                .height(180.0).rating(0.0).completedOrders(0).user(user).build());
    }

    @Test
    void createExecutorSuccessfully() {
        Executor executor = new Executor("john", "123456", 75.0, 180.0,0.0,0,buildCreateUser());
        Executor createdExecutor = executorService.createExecutor(executor);

        assertNotNull(createdExecutor);
        assertExecutorDetails(createdExecutor, "john", "123456", 75.0, 180.0, 0.0, 0);
    }

    @Test
    void getExecutorByIdSuccessfully() {
        buildCreateExecutor(buildCreateUser());

        Executor fetchedExecutor = executorService.getExecutorById("john");

        assertNotNull(fetchedExecutor);
        assertExecutorDetails(fetchedExecutor, "john", "123456", 75.0, 180.0, 0.0, 0);
    }

    @Test
    void updateExecutorSuccessfully() {
        buildCreateExecutor(buildCreateUser());

        ExecutorRequest updateRequest = new ExecutorRequest("john", "654321", 80.0, 185.0);
        Executor updatedExecutor = executorService.updateExecutor(updateRequest);

        assertNotNull(updatedExecutor);
        assertExecutorDetails(updatedExecutor, "john", "654321", 80.0, 185.0, 0.0, 0);
    }


    private void assertExecutorDetails(Executor executor, String expectedUsername, String expectedPassportNumber,
                                       double expectedWeight, double expectedHeight, double expectedRating, int expectedCompletedOrders) {
        assertEquals(expectedUsername, executor.getUsername());
        assertEquals(expectedPassportNumber, executor.getPassportSeriesNumber());
        assertEquals(expectedWeight, executor.getWeight());
        assertEquals(expectedHeight, executor.getHeight());
        assertEquals(expectedRating, executor.getRating());
        assertEquals(expectedCompletedOrders, executor.getCompletedOrders());
    }
}
