package org.example.sports.mapper;

import org.example.sports.controller.executor.dto.ExecutorDto;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.model.Executor;
import org.example.sports.service.UserService;
import org.example.sports.util.Models;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.example.sports.util.Models.EXECUTOR;
import static org.example.sports.util.Models.EXECUTOR_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ExecutorMapperTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private ExecutorMapper executorMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertToDto() {
        Executor executor = EXECUTOR();

        ExecutorDto executorDto = executorMapper.convertToDto(executor);

        assertEquals(executor.getUsername(), executorDto.username());
        assertEquals(executor.getPassportSeriesNumber(), executorDto.passportSeriesNumber());
        assertEquals(executor.getWeight(), executorDto.weight());
        assertEquals(executor.getHeight(), executorDto.height());
        assertEquals(executor.getRating(), executorDto.rating());
        assertEquals(executor.getCompletedOrders(), executorDto.completedOrders());
    }
    @Test
    void testConvert() {
        ExecutorRequest executorRequest = EXECUTOR_REQUEST();

        when(userService.getUserById(any(String.class))).thenReturn(Models.USER_EXECUTOR());

        Executor executor = executorMapper.convert(executorRequest);

        assertEquals(executorRequest.username(), executor.getUsername());
        assertEquals(executorRequest.passportSeriesNumber(), executor.getPassportSeriesNumber());
        assertEquals(executorRequest.weight(), executor.getWeight());
        assertEquals(executorRequest.height(), executor.getHeight());
    }
}
