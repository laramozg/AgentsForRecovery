//package org.example.sports.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.sports.controller.fight.dto.CreateFight;
//import org.example.sports.controller.fight.dto.FightDto;
//import org.example.sports.mapper.FightMapper;
//import org.example.sports.model.Executor;
//import org.example.sports.model.Fight;
//import org.example.sports.model.Order;
//import org.example.sports.model.enums.FightStatus;
//import org.example.sports.service.FightService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class FightControllerTest {
//
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private FightService fightService;
//
//    @MockBean
//    private FightMapper fightMapper;
//
//    private CreateFight createFight;
//    private FightDto fightDto;
//    private Fight fight;
//
//    @BeforeEach
//    void setUp() {
//        fightDto = new FightDto(1L, "user1", 1L, "PENDING");
//        createFight = new CreateFight("user1", 1L);
//        fight = Fight.builder().id(1L).order(Order.builder().id(1L).build()).executor(Executor.builder()
//                        .username("user1").build())
//                .status(FightStatus.PENDING).build();
//    }
//
//    @Test
//    void testGetFightsByExecutorIdSuccess() throws Exception {
//        List<Fight> fights = List.of(fight);
//        Page<Fight> fightPage = new PageImpl<>(fights);
//
//        when(fightService.getFightsByExecutorId(anyString(), anyInt(), anyInt())).thenReturn(fightPage);
//        when(fightMapper.convertToDto(any(Fight.class))).thenReturn(fightDto);
//
//        mockMvc.perform(get("/api/v1/sports/user/fight/user1")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].id", is(1)))
//                .andExpect(jsonPath("$.content[0].executorId", is("user1")))
//                .andExpect(jsonPath("$.content[0].orderId", is(1)))
//                .andExpect(jsonPath("$.content[0].status", is("PENDING")));
//    }
//
//    @Test
//    void testCreateFightSuccess() throws Exception {
//        when(fightMapper.convert(any(CreateFight.class))).thenReturn(fight);
//        when(fightService.createFight(any(Fight.class))).thenReturn(fight);
//        when(fightMapper.convertToDto(any(Fight.class))).thenReturn(fightDto);
//
//        mockMvc.perform(post("/api/v1/sports/user/fight")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createFight)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.executorId", is("user1")))
//                .andExpect(jsonPath("$.orderId", is(1)))
//                .andExpect(jsonPath("$.status", is("PENDING")));
//    }
//
//    @Test
//    void testUpdateFightStatusSuccess() throws Exception {
//        when(fightService.updateFightStatus(any(Long.class), any(FightStatus.class))).thenReturn(fight);
//        when(fightMapper.convertToDto(any(Fight.class))).thenReturn(fightDto);
//
//        mockMvc.perform(put("/api/v1/sports/user/fight/1/status")
//                        .param("newStatus", "PENDING"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.executorId", is("user1")))
//                .andExpect(jsonPath("$.orderId", is(1)))
//                .andExpect(jsonPath("$.status", is("PENDING")));
//    }
//
//}
