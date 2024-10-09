package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.sports.BaseIntegrationTest;
import org.example.sports.controller.order.dto.CreateOrderRequest;
import org.example.sports.util.Models;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class OrderControllerTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final CreateOrderRequest createOrderRequest = Models.CREATE_ORDER_REQUEST();

    @Test
    void testCreateOrderSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_PREFIX + "user/order")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(createOrderRequest.username()))
                .andExpect(jsonPath("$.cityId").value(createOrderRequest.cityId()))
                .andExpect(jsonPath("$.victimId").value(createOrderRequest.victimId()))
                .andExpect(jsonPath("$.state").value("WAITING"));
    }

    @Test
    void testGetOrdersWithStatusWaitSuccess() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/order")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value(createOrderRequest.username()))
                .andExpect(jsonPath("$.content[0].cityId").value(createOrderRequest.cityId()))
                .andExpect(jsonPath("$.content[0].victimId").value(createOrderRequest.victimId()))
                .andExpect(jsonPath("$.content[0].state").value("WAITING"));
    }

    @Test
    void testGetOrdersByUsernameSuccess() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/order/{username}", createOrderRequest.username())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value(createOrderRequest.username()))
                .andExpect(jsonPath("$.content[0].cityId").value(createOrderRequest.cityId()))
                .andExpect(jsonPath("$.content[0].victimId").value(createOrderRequest.victimId()))
                .andExpect(jsonPath("$.content[0].state").value("WAITING"));
    }

    @Test
    void testDeleteOrderSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_PREFIX + "user/order/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetOrdersWithSizeGreaterThan50() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/order")
                        .param("page", "0")
                        .param("size", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(50)));
    }
}
