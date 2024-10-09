package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.sports.BaseIntegrationTest;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.example.sports.util.Models.CREATE_VICTIM_REQUEST;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class VictimControllerTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final CreateVictimRequest createVictimRequest = CREATE_VICTIM_REQUEST();

    @Test
    void testCreateVictimSuccess() throws Exception {
        mockMvc.perform(post(API_PREFIX + "user/victim")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVictimRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(createVictimRequest.firstName())))
                .andExpect(jsonPath("$.lastName", is(createVictimRequest.lastName())))
                .andExpect(jsonPath("$.phone", is(createVictimRequest.phone())));
    }

    @Test
    void testGetAllVictimsSuccess() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/victim")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName", is(createVictimRequest.firstName())));
    }

    @Test
    void testGetVictimSuccess() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/victim/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(createVictimRequest.firstName())))
                .andExpect(jsonPath("$.lastName", is(createVictimRequest.lastName())));
    }


    @Test
    void testDeleteVictimSuccess() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "user/victim/{id}", 1L))
                .andExpect(status().isNoContent());

    }
}