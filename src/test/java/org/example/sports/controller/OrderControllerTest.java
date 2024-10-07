package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.order.dto.OrderDto;
import org.example.sports.mapper.OrderMapper;
import org.example.sports.model.City;
import org.example.sports.model.Order;
import org.example.sports.model.User;
import org.example.sports.model.Victim;
import org.example.sports.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private OrderService orderService;

    private CreateOrderRequest createOrderRequest;
    private Order order;
    private OrderDto orderDto;
    private final LocalDate today = LocalDate.now().plusDays(5);

    @BeforeEach
    void setUp() {
        createOrderRequest = new CreateOrderRequest("user1", 1L, 1L, today, null);
        orderDto = new OrderDto(1L, "user1", 1L, 1L, today, "WAITING", null);
        order = Order.builder().id(1L).user(User.builder().username("user1").build()).city(City.builder().id(1L).build())
                .victim(Victim.builder().id(1L).build()).deadline(LocalDate.now().plusDays(3))
                .mutilations(Collections.emptySet()).build();
    }

    @Test
    void testCreateOrderSuccess() throws Exception {
        when(orderMapper.convert(any(CreateOrderRequest.class))).thenReturn(order);
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        when(orderMapper.convertToDto(any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sports/user/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.cityId", is(1)))
                .andExpect(jsonPath("$.victimId", is(1)))
                .andExpect(jsonPath("$.state", is("WAITING")));
    }

    @Test
    void testGetOrdersWithStatusWaitSuccess() throws Exception {
        Page<Order> orderPage = new PageImpl<>(List.of(order));

        when(orderService.getOrdersWithStatusWait(anyInt(), anyInt())).thenReturn(orderPage);
        when(orderMapper.convertToDto(any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(get("/api/v1/sports/user/order")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].username", is("user1")))
                .andExpect(jsonPath("$.content[0].cityId", is(1)))
                .andExpect(jsonPath("$.content[0].victimId", is(1)))
                .andExpect(jsonPath("$.content[0].deadline", is(today.toString())))
                .andExpect(jsonPath("$.content[0].state", is("WAITING")));
    }

    @Test
    void testGetOrdersByUsernameSuccess() throws Exception {
        Page<Order> orderPage = new PageImpl<>(List.of(order));

        when(orderService.getOrdersByUsername(anyString(), anyInt(), anyInt())).thenReturn(orderPage);
        when(orderMapper.convertToDto(any(Order.class))).thenReturn(orderDto);

        mockMvc.perform(get("/api/v1/sports/user/order/user1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].username", is("user1")))
                .andExpect(jsonPath("$.content[0].cityId", is(1)))
                .andExpect(jsonPath("$.content[0].victimId", is(1)))
                .andExpect(jsonPath("$.content[0].deadline", is(today.toString())));
    }

    @Test
    void testDeleteOrderSuccess() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/sports/user/order/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    void testGetOrdersWithSizeGreaterThan50() throws Exception {

        Page<Order> mockOrders = new PageImpl<>(List.of());
        when(orderService.getOrdersWithStatusWait(anyInt(), anyInt()))
                .thenReturn(mockOrders);

        mockMvc.perform(get("/api/v1/sports/user/order")
                        .param("page", "0")
                        .param("size", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(result -> {
                    Mockito.verify(orderService).getOrdersWithStatusWait(0, 50);
                });
    }
}
