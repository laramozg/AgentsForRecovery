package org.example.sports.mapper;

import org.example.sports.controller.fight.dto.CreateFight;
import org.example.sports.controller.fight.dto.FightDto;
import org.example.sports.model.Executor;
import org.example.sports.model.Fight;
import org.example.sports.model.Order;
import org.example.sports.service.ExecutorService;
import org.example.sports.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.example.sports.util.Models.*;
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
        CreateFight createFight = CREATE_FIGHT_REQUEST();
        Executor executor = EXECUTOR();
        Order order = ORDER();

        when(executorService.getExecutorById(createFight.executorId())).thenReturn(executor);
        when(orderService.getOrderById(createFight.orderId())).thenReturn(order);

        Fight fight = fightMapper.convert(createFight);

        assertEquals(executor, fight.getExecutor());
        assertEquals(order, fight.getOrder());
    }

    @Test
    void testConvertToDto() {
        Fight fight = FIGHT();

        FightDto fightDto = fightMapper.convertToDto(fight);

        assertEquals(fight.getId(), fightDto.id());
        assertEquals(fight.getExecutor().getUsername(), fightDto.executorId());
        assertEquals(fight.getOrder().getId(), fightDto.orderId());
        assertEquals(fight.getStatus().toString(), fightDto.status());
    }
}
