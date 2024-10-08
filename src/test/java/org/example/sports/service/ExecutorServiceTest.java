package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.model.Executor;
import org.example.sports.repositore.ExecutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.example.sports.util.Models.EXECUTOR;
import static org.example.sports.util.Models.EXECUTOR_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ExecutorServiceTest {

    @InjectMocks
    private ExecutorService executorService;

    @Mock
    private ExecutorRepository executorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExecutor() {
        Executor executor = EXECUTOR();

        when(executorRepository.save(any(Executor.class))).thenReturn(executor);

        Executor result = executorService.createExecutor(executor);

        assertNotNull(result);
        assertEquals(executor.getUsername(), result.getUsername());
        assertEquals(executor.getCompletedOrders(), result.getCompletedOrders());
        assertEquals(executor.getWeight(), result.getWeight());
    }

    @Test
    void testGetExecutorByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> executorService.getExecutorById("non_existing_user"));
    }

    @Test
    void testUpdateExecutor() {
        Executor executor = EXECUTOR();

        when(executorRepository.findById(anyString())).thenReturn(Optional.of(executor));
        when(executorRepository.save(any(Executor.class))).thenReturn(executor);

        Executor updatedExecutor = executorService.updateExecutor(EXECUTOR_REQUEST());

        assertEquals(executor.getPassportSeriesNumber(), updatedExecutor.getPassportSeriesNumber());
        assertEquals(executor.getWeight(), updatedExecutor.getWeight());
        assertEquals(executor.getHeight(), updatedExecutor.getHeight());
    }
}
