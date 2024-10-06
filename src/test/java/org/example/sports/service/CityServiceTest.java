package org.example.sports.service;

import org.example.sports.controller.city.dto.CityDto;
import org.example.sports.controller.city.dto.CreateCity;
import org.example.sports.model.City;
import org.example.sports.repositore.CityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CityServiceTest extends AbstractServiceTest {

    @Autowired
    private CityService cityService;

    @Autowired
    private CityRepository cityRepository;

    @AfterEach
    void tearDown() {
        cityRepository.deleteAll();
    }

    private City buildCreateCity(String name, String region) {
        return cityRepository.save(City.builder().name(name).region(region).build());
    }

    @Test
    void createCitySuccessfully() {
        CreateCity createCity = new CreateCity("New York", "New York State");

        CityDto createdCity = cityService.createCity(createCity);

        assertNotNull(createdCity);
        assertEquals("New York", createdCity.name());
        assertEquals("New York State", createdCity.region());
    }

    @Test
    void findAllCitiesSuccessfully() {
        buildCreateCity("Los Angeles", "California");
        buildCreateCity("Chicago", "Illinois");

        Page<CityDto> cities = cityService.findAllCities(0, 10);

        assertNotNull(cities);
        assertEquals(2, cities.getTotalElements());
    }

    @Test
    void countCitiesSuccessfully() {
        buildCreateCity("Los Angeles", "California");
        buildCreateCity("Chicago", "Illinois");

        long count = cityService.countCities();

        assertEquals(2, count);
    }

    @Test
    void deleteCitySuccessfully() {
        City city = buildCreateCity("Los Angeles", "California");

        cityService.deleteCity(city.getId());

        assertEquals(0, cityService.countCities());
    }
}
