package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.model.City;
import org.example.sports.repositore.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.example.sports.util.Models.CITY;
import static org.example.sports.util.Models.CREATE_CITY_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCity_ShouldSaveAndReturnCity() {
        City city = CITY();

        when(cityRepository.save(any(City.class))).thenReturn(city);

        City result = cityService.createCity(CREATE_CITY_REQUEST());

        assertNotNull(result);
        assertEquals(city.getName(), result.getName());
        assertEquals(city.getRegion(), result.getRegion());
    }

    @Test
    void findAllCities_ShouldReturnPageOfCities() {
        City city1 = CITY();
        City city2 = City.builder().name("Another City").region("Another Region").build();
        Page<City> cityPage = new PageImpl<>(List.of(city1, city2));

        when(cityRepository.findAll(any(Pageable.class))).thenReturn(cityPage);

        Page<City> result = cityService.findAllCities(0, 2);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(city1.getName(), result.getContent().get(0).getName());
        assertEquals(city2.getName(), result.getContent().get(1).getName());
    }

    @Test
    void getCityById_ShouldReturnCity_WhenCityExists() {
        City city = CITY();
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));

        City result = cityService.getCityById(1L);

        assertNotNull(result);
        assertEquals(city.getName(), result.getName());
    }

    @Test
    void getCityById_ShouldThrowException_WhenCityNotFound() {
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            cityService.getCityById(1L);
        });

        assertEquals("City not found", exception.getMessage());
    }

    @Test
    void countCities_ShouldReturnCityCount() {
        when(cityRepository.count()).thenReturn(5L);

        long result = cityService.countCities();

        assertEquals(5L, result);
    }

    @Test
    void deleteCity_ShouldDeleteCityById() {
        doNothing().when(cityRepository).deleteById(anyLong());

        cityService.deleteCity(1L);

        verify(cityRepository, times(1)).deleteById(1L);
    }

}
