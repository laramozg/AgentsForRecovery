package org.example.sports.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sports.controller.city.dto.CreateCity;
import org.example.sports.model.City;
import org.example.sports.service.CityService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CityService cityService;

    private CreateCity createCity;
    private City city;

    @BeforeEach
    void setUp() {
        createCity = new CreateCity("New City", "New Region");
        city = new City(1L, "New City", "New Region");

    }

    @Test
    void testCreateCitySuccess() throws Exception {
        when(cityService.createCity(any(CreateCity.class))).thenReturn(city);

        mockMvc.perform(post("/api/v1/sports/user/city/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCity)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New City")))
                .andExpect(jsonPath("$.region", is("New Region")));
    }

    @Test
    void testGetAllCitiesSuccess() throws Exception {
        Page<City> cityPage = new PageImpl<>(Collections.singletonList(city));

        when(cityService.findAllCities(anyInt(), anyInt())).thenReturn(cityPage);
        when(cityService.countCities()).thenReturn(1L);

        mockMvc.perform(get("/api/v1/sports/user/city")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "1"))
                .andExpect(jsonPath("$.content[0].name", is("New City")))
                .andExpect(jsonPath("$.content[0].region", is("New Region")));
        ;
    }

    @Test
    void testDeleteCitySuccess() throws Exception {
        doNothing().when(cityService).deleteCity(1L);

        mockMvc.perform(delete("/api/v1/sports/user/city/change/1"))
                .andExpect(status().isNoContent());

        verify(cityService, times(1)).deleteCity(1L);
    }
}