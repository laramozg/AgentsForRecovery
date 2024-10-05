package org.example.sports.service;

import jakarta.transaction.Transactional;
import org.example.sports.controller.city.dto.CityDto;
import org.example.sports.controller.city.dto.CreateCity;
import org.example.sports.repositore.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CityServiceTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private CityService cityService;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        cityRepository.deleteAll();
    }

    @Test
    void testCreateCity() {
        CreateCity createCity = new CreateCity("New York", "New York State");

        CityDto createdCity = cityService.createCity(createCity);

        assertNotNull(createdCity);
        assertEquals("New York", createdCity.name());
        assertEquals("New York State", createdCity.region());
    }

    @Test
    void testFindAllCities() {
        cityService.createCity(new CreateCity("Los Angeles", "California"));
        cityService.createCity(new CreateCity("Chicago", "Illinois"));

        Page<CityDto> cities = cityService.findAllCities(0, 10);

        assertNotNull(cities);
        assertEquals(2, cities.getTotalElements());
    }

    @Test
    void testCountCities() {
        cityService.createCity(new CreateCity("Los Angeles", "California"));
        cityService.createCity(new CreateCity("Chicago", "Illinois"));

        long count = cityService.countCities();

        assertEquals(2, count);
    }

    @Test
    void testDeleteCity() {
        CityDto cityDto = cityService.createCity(new CreateCity("Los Angeles", "California"));

        cityService.deleteCity(cityDto.id());

        assertEquals(0, cityService.countCities());
    }
}
