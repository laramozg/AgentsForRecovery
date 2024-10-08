package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.BaseIntegrationTest;
import org.example.sports.controller.city.dto.CreateCity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.example.sports.util.Models.CREATE_CITY_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CityControllerTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final CreateCity createUserRequest = CREATE_CITY_REQUEST();


    @Test
    void createCityTestSuccess() throws Exception {
        mockMvc.perform(post(API_PREFIX + "user/city/change")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createUserRequest.name()))
                .andExpect(jsonPath("$.region").value(createUserRequest.region()));

    }

    @Test
    void getAllCitiesTestSuccess() throws Exception {
        mockMvc.perform(get(API_PREFIX + "user/city")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"))
                .andExpect(jsonPath("$.content").isArray());
    }


    @Test
    void deleteCityTestSuccess() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "user/city/change/{id}", 1L))
                .andExpect(status().isNoContent());

    }
}