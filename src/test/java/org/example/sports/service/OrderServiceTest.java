package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.model.Order;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.repositore.OrderRepository;
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

import static org.example.sports.util.Models.ORDER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        Order order = ORDER();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testGetOrdersWithStatusWait() {
        Order order = ORDER();
        Page<Order> page = new PageImpl<>(Collections.singletonList(order));
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findAllByStatus(OrderStatus.WAITING, pageable)).thenReturn(page);

        Page<Order> result = orderService.getOrdersWithStatusWait(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(OrderStatus.WAITING, result.getContent().get(0).getStatus());
    }

    @Test
    void testGetOrderByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void testDeleteOrder() {
        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }
}