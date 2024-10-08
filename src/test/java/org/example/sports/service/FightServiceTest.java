package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.model.Executor;
import org.example.sports.model.Fight;
import org.example.sports.model.Order;
import org.example.sports.model.enums.FightStatus;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.repositore.FightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.example.sports.util.Models.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class FightServiceTest {

    @InjectMocks
    private FightService fightService;

    @Mock
    private OrderService orderService;

    @Mock
    private ExecutorService executorService;

    @Mock
    private FightRepository fightRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFight() {
        Fight fight = FIGHT();

        when(orderService.getOrderById(anyLong())).thenReturn(ORDER());
        when(fightRepository.save(any(Fight.class))).thenReturn(fight);

        Fight result = fightService.createFight(fight);

        assertNotNull(result);
        assertEquals(FightStatus.PENDING, result.getStatus());
    }

    @Test
    void testGetFightByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> fightService.getFightById(1L));
    }

    @Test
    void testGetFightsByExecutorId() {
        Fight fight = FIGHT();

        Page<Fight> page = new PageImpl<>(Collections.singletonList(fight));
        Pageable pageable = PageRequest.of(0, 10);
        when(fightRepository.findByExecutor_Username(fight.getExecutor().getUsername(), pageable)).thenReturn(page);

        Page<Fight> result = fightService.getFightsByExecutorId(fight.getExecutor().getUsername(), 0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(fight, result.getContent().get(0));
    }

    @Test
    void testGetFightById() {
        Fight fight = FIGHT();

        when(fightRepository.findById(anyLong())).thenReturn(Optional.of(fight));

        Fight result = fightService.getFightById(1L);

        assertEquals(fight, result);
    }

    @Test
    void testUpdateFightStatus_Victory() {
        Fight fight = FIGHT();
        Order order = ORDER();
        Executor executor = EXECUTOR();

        when(fightRepository.findById(anyLong())).thenReturn(Optional.of(fight));
        when(orderService.getOrderById(anyLong())).thenReturn(order);
        when(executorService.getExecutorById(anyString())).thenReturn(executor);
        when(fightRepository.save(any(Fight.class))).thenReturn(fight);
        when(fightRepository.countByExecutorUsernameAndStatus(anyString(), eq(FightStatus.VICTORY))).thenReturn(1L);
        when(fightRepository.countByExecutorUsername(anyString())).thenReturn(1L);

        Fight updatedFight = fightService.updateFightStatus(1L, FightStatus.VICTORY);

        assertEquals(10.0, executor.getRating());
        assertEquals(1, executor.getCompletedOrders());
        assertEquals(FightStatus.VICTORY, updatedFight.getStatus());
        assertEquals(OrderStatus.DONE, order.getStatus());

    }

    @Test
    void testUpdateFightStatus_Loss() {
        Fight fight = FIGHT();
        Order order = ORDER();
        Executor executor = EXECUTOR();

        when(fightRepository.findById(anyLong())).thenReturn(Optional.of(fight));
        when(orderService.getOrderById(anyLong())).thenReturn(order);
        when(executorService.getExecutorById(anyString())).thenReturn(executor);
        when(fightRepository.save(any(Fight.class))).thenReturn(fight);
        when(fightRepository.countByExecutorUsernameAndStatus(anyString(), eq(FightStatus.VICTORY))).thenReturn(0L);
        when(fightRepository.countByExecutorUsername(anyString())).thenReturn(1L);

        Fight updatedFight = fightService.updateFightStatus(1L, FightStatus.LOSS);

        assertEquals(0.0, executor.getRating());
        assertEquals(0, executor.getCompletedOrders());
        assertEquals(FightStatus.LOSS, updatedFight.getStatus());
        assertEquals(OrderStatus.WAITING, order.getStatus());
    }

}
