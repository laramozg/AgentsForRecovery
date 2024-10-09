package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.sports.BaseIntegrationTest;
import org.example.sports.controller.fight.dto.CreateFight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.example.sports.util.Models.CREATE_FIGHT_REQUEST;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class FightControllerTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final CreateFight createFight = CREATE_FIGHT_REQUEST();


    @Test
    void testGetFightsByExecutorIdSuccess() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/fight/{executorId}", createFight.executorId())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].executorId").value(createFight.executorId()))
                .andExpect(jsonPath("$.content[0].orderId").value(createFight.orderId()))
                .andExpect(jsonPath("$.content[0].status", is("PENDING")));
    }

    @Test
    void testCreateFightSuccess() throws Exception {
        mockMvc.perform(post(API_PREFIX + "user/fight")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.executorId").value(createFight.executorId()))
                .andExpect(jsonPath("$.orderId").value(createFight.orderId()))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    void testUpdateFightStatusSuccess() throws Exception {
        mockMvc.perform(put(API_PREFIX + "user/fight/1/status")
                        .param("newStatus", "VICTORY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.executorId").value(createFight.executorId()))
                .andExpect(jsonPath("$.orderId").value(createFight.orderId()))
                .andExpect(jsonPath("$.status", is("VICTORY")));
    }

}
