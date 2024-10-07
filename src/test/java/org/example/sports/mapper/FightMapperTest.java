package org.example.sports.mapper;

import org.example.sports.controller.fight.dto.CreateFight;
import org.example.sports.controller.fight.dto.FightDto;
import org.example.sports.model.Executor;
import org.example.sports.model.Fight;
import org.example.sports.model.Order;
import org.example.sports.model.enums.FightStatus;
import org.example.sports.service.ExecutorService;
import org.example.sports.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class FightMapperTest {
    @Mock
    private ExecutorService executorService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private FightMapper fightMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvert() {
        CreateFight createFight = new CreateFight("executor1", 2L);
        Executor executor = Executor.builder().username("executor1").build();
        Order order = Order.builder().id(2L).build();

        when(executorService.getExecutorById("executor1")).thenReturn(executor);
        when(orderService.getOrderById(2L)).thenReturn(order);

        Fight fight = fightMapper.convert(createFight);

        assertEquals(executor, fight.getExecutor());
        assertEquals(order, fight.getOrder());
    }

    @Test
    void testConvertToDto() {
        Executor executor = Executor.builder().username("executor1").build();
        Order order = Order.builder().id(2L).build();
        Fight fight = Fight.builder().id(3L).executor(executor).order(order).status(FightStatus.PENDING).build();

        FightDto fightDto = fightMapper.convertToDto(fight);

        assertEquals(3L, fightDto.id());
        assertEquals("executor1", fightDto.executorId());
        assertEquals(2L, fightDto.orderId());
        assertEquals(FightStatus.PENDING.name(), fightDto.status());
    }
}
