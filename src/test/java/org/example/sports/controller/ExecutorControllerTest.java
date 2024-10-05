package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.controller.executor.dto.ExecutorDto;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.service.ExecutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExecutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExecutorService executorService;

    private ExecutorRequest executorRequest;
    private ExecutorDto executorDto;

    @BeforeEach
    void setUp() {
        executorRequest = new ExecutorRequest("user1", "user1",55.5,155.5);
        executorDto = new ExecutorDto("user1", "123456",55.5,155.5, 0.0, 0);

    }

    @Test
    void testCreateExecutor() throws Exception {
        Mockito.when(executorService.createExecutor(any(ExecutorRequest.class))).thenReturn(executorDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sports/user/executor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(executorRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.weight", is(55.5)))
                .andExpect(jsonPath("$.rating", is(0.0)));
    }

    @Test
    void testGetExecutor() throws Exception {
        Mockito.when(executorService.getUser("user1")).thenReturn(executorDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/sports/user/executor/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight", is(55.5)))
                .andExpect(jsonPath("$.rating", is(0.0)));
    }

    @Test
    void testUpdateExecutor() throws Exception {
        Mockito.when(executorService.updateUser(eq("user1"), any(ExecutorRequest.class))).thenReturn(executorDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/sports/user/executor/user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(executorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight", is(55.5)))
                .andExpect(jsonPath("$.rating", is(0.0)));
    }
}