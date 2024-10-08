//package org.example.sports.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.sports.controller.executor.dto.ExecutorDto;
//import org.example.sports.controller.executor.dto.ExecutorRequest;
//import org.example.sports.mapper.ExecutorMapper;
//import org.example.sports.model.Executor;
//import org.example.sports.model.User;
//import org.example.sports.model.enums.Role;
//import org.example.sports.service.ExecutorService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class ExecutorControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ExecutorService executorService;
//
//    @MockBean
//    private ExecutorMapper executorMapper;
//
//    private ExecutorRequest executorRequest;
//    private Executor executor;
//    private ExecutorDto executorDto;
//
//    @BeforeEach
//    void setUp() {
//        User user = new User("user1", "user1", "@user1", Role.EXECUTOR, null);
//        executorRequest = new ExecutorRequest("user1", "user1", 55.5, 155.5);
//        executor = new Executor("user1", "123456", 55.5, 155.5, 0.0, 0, user);
//        executorDto = new ExecutorDto("user1", "123456", 55.5, 155.5, 0.0, 0);
//    }
//
//    @Test
//    void testCreateExecutorSuccess() throws Exception {
//        when(executorService.createExecutor(any(Executor.class))).thenReturn(executor);
//        when(executorMapper.convert(any(ExecutorRequest.class))).thenReturn(executor);
//        when(executorMapper.convertToDto(any(Executor.class))).thenReturn(executorDto);
//
//        mockMvc.perform(post("/api/v1/sports/user/executor")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(executorRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username", is("user1")))
//                .andExpect(jsonPath("$.weight", is(55.5)))
//                .andExpect(jsonPath("$.rating", is(0.0)));
//    }
//
//    @Test
//    void testGetExecutorSuccess() throws Exception {
//        when(executorService.getExecutorById("user1")).thenReturn(executor);
//        when(executorMapper.convertToDto(any(Executor.class))).thenReturn(executorDto);
//
//        mockMvc.perform(get("/api/v1/sports/user/executor/user1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.weight", is(55.5)))
//                .andExpect(jsonPath("$.rating", is(0.0)));
//    }
//
//    @Test
//    void testUpdateExecutorSuccess() throws Exception {
//        when(executorService.updateExecutor(any(ExecutorRequest.class))).thenReturn(executor);
//        when(executorMapper.convertToDto(any(Executor.class))).thenReturn(executorDto);
//
//        mockMvc.perform(put("/api/v1/sports/user/executor")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(executorRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.weight", is(55.5)))
//                .andExpect(jsonPath("$.rating", is(0.0)));
//    }
//}