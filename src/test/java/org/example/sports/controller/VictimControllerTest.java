package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
import org.example.sports.model.Victim;
import org.example.sports.service.VictimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VictimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VictimService victimService;

    private CreateVictimRequest createVictimRequest;
    private Victim victim;

    @BeforeEach
    void setUp() {
        createVictimRequest = new CreateVictimRequest("John", "Doe", "workplace",
                "position", "residence", "123456", "description");
        victim = new Victim(1L, "John", "Doe", "workplace",
                "position", "residence", "123456", "description");
    }

    @Test
    void testCreateVictimSuccess() throws Exception {
        when(victimService.createVictim(any(CreateVictimRequest.class))).thenReturn(victim);

        mockMvc.perform(post("/api/v1/sports/user/victim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVictimRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.phone", is("123456")));
    }

    @Test
    void testGetVictimSuccess() throws Exception {
        when(victimService.getVictimById(1L)).thenReturn(victim);

        mockMvc.perform(get("/api/v1/sports/user/victim/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.phone", is("123456")));
    }

    @Test
    void testGetAllVictimsSuccess() throws Exception {
        Page<Victim> victimPage = new PageImpl<>(Collections.singletonList(victim));

        when(victimService.getAllVictims(anyInt(), anyInt())).thenReturn(victimPage);

        mockMvc.perform(get("/api/v1/sports/user/victim")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")))
                .andExpect(jsonPath("$.content[0].lastName", is("Doe")))
                .andExpect(jsonPath("$.content[0].phone", is("123456")));
    }

    @Test
    void testDeleteVictimSuccess() throws Exception {
        doNothing().when(victimService).deleteVictim(1L);

        mockMvc.perform(delete("/api/v1/sports/user/victim/1"))
                .andExpect(status().isNoContent());

        verify(victimService, times(1)).deleteVictim(1L);
    }
}