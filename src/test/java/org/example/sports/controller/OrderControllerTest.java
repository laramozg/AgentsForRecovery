package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.controller.order.dto.OrderDto;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
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
    private OrderService orderService;

    private CreateOrderRequest createOrderRequest;
    private OrderDto orderDto;
    private final LocalDate today = LocalDate.now().plusDays(5);

    @BeforeEach
    void setUp() {
        createOrderRequest = new CreateOrderRequest("user2", 1L, 1L, today, Arrays.asList(1L, 2L));
        List<MutilationDto> mutilations = Arrays.asList(new MutilationDto(1L, "Broken Arm", 10000), new MutilationDto(2L, "Scar", 15000));
        orderDto = new OrderDto(1L, "user123", 1L, 1L, today, "WAITING", mutilations);

    }

    @Test
    void testCreateOrder() throws Exception {
        Mockito.when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(orderDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sports/user/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("user123")))
                .andExpect(jsonPath("$.cityId", is(1)))
                .andExpect(jsonPath("$.victimId", is(1)))
                .andExpect(jsonPath("$.state", is("WAITING")))
                .andExpect(jsonPath("$.mutilations[0].id", is(1)))
                .andExpect(jsonPath("$.mutilations[0].type", is("Broken Arm")))
                .andExpect(jsonPath("$.mutilations[0].price", is(10000)));
    }

    @Test
    void testGetOrdersWithStatusWait() throws Exception {
        Page<OrderDto> orderPage = new PageImpl<>(Collections.singletonList(orderDto));

        Mockito.when(orderService.getOrdersWithStatusWait(anyInt(), anyInt())).thenReturn(orderPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/sports/user/order")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].username", is("user123")))
                .andExpect(jsonPath("$.content[0].cityId", is(1)))
                .andExpect(jsonPath("$.content[0].victimId", is(1)))
                .andExpect(jsonPath("$.content[0].deadline", is(today.toString())))
                .andExpect(jsonPath("$.content[0].state", is("WAITING")))
                .andExpect(jsonPath("$.content[0].mutilations[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].mutilations[0].type", is("Broken Arm")));
    }

    @Test
    void testGetOrdersByUsername() throws Exception {
        Page<OrderDto> orderPage = new PageImpl<>(Collections.singletonList(orderDto));

        Mockito.when(orderService.getOrdersByUsername(anyString(), anyInt(), anyInt())).thenReturn(orderPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/sports/user/order/user123")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].username", is("user123")))
                .andExpect(jsonPath("$.content[0].cityId", is(1)))
                .andExpect(jsonPath("$.content[0].victimId", is(1)))
                .andExpect(jsonPath("$.content[0].deadline", is(today.toString())))
                .andExpect(jsonPath("$.content[0].mutilations[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].mutilations[0].type", is("Broken Arm")));
    }

    @Test
    void testDeleteOrder() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/sports/user/order/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(orderService, times(1)).deleteOrder(1L);
    }
}
