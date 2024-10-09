package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.sports.BaseIntegrationTest;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.example.sports.util.Models.EXECUTOR_REQUEST;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class ExecutorControllerTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final ExecutorRequest executorRequest = EXECUTOR_REQUEST();

    @Test
    void testCreateExecutorSuccess() throws Exception {
        mockMvc.perform(post(API_PREFIX + "user/executor")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(executorRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(executorRequest.username())))
                .andExpect(jsonPath("$.weight", is(executorRequest.weight())))
                .andExpect(jsonPath("$.rating", is(0.0)));
    }

    @Test
    void testGetExecutorSuccess() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/executor/{username}", executorRequest.username()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight", is(executorRequest.weight())))
                .andExpect(jsonPath("$.rating", is(0.0)));
    }

    @Test
    void testUpdateExecutorSuccess() throws Exception {
        mockMvc.perform(put(API_PREFIX + "user/executor")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(executorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight", is(executorRequest.weight())))
                .andExpect(jsonPath("$.rating", is(0.0)));
    }
}