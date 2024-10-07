package org.example.sports.mapper;

import org.example.sports.controller.executor.dto.ExecutorDto;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.model.Executor;
import org.example.sports.model.User;
import org.example.sports.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Executor executor = Executor.builder()
                .username("testExecutor")
                .passportSeriesNumber("123456789")
                .weight(80.0)
                .height(180.0)
                .rating(4.5)
                .completedOrders(10)
                .build();

        ExecutorDto executorDto = executorMapper.convertToDto(executor);

        assertEquals("testExecutor", executorDto.username());
        assertEquals("123456789", executorDto.passportSeriesNumber());
        assertEquals(80.0, executorDto.weight());
        assertEquals(180.0, executorDto.height());
        assertEquals(4.5, executorDto.rating());
        assertEquals(10, executorDto.completedOrders());
    }
    @Test
    void testConvert() {
        ExecutorRequest executorRequest = new ExecutorRequest("testUser", "123456789", 80.0, 180.0);

        User user = User.builder().username("testUser").build();

        when(userService.getUserById("testUser")).thenReturn(user);

        Executor executor = executorMapper.convert(executorRequest);

        assertEquals("testUser", executor.getUsername());
        assertEquals("123456789", executor.getPassportSeriesNumber());
        assertEquals(80.0, executor.getWeight());
        assertEquals(180.0, executor.getHeight());
        assertEquals(user, executor.getUser());
    }
}
