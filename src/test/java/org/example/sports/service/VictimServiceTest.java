package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
import org.example.sports.controller.victim.dto.VictimDto;
import org.example.sports.repositore.VictimRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class VictimServiceTest {

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
    private VictimService victimService;

    @Autowired
    private VictimRepository victimRepository;

    @BeforeEach
    void setUp() {
        victimRepository.deleteAll();
    }

    @Test
    void testCreateVictim() {
        CreateVictimRequest request = new CreateVictimRequest(
                "John", "Doe", "Company", "Manager", "New York", "555-1234", "Injured");

        VictimDto createdVictim = victimService.createVictim(request);

        assertNotNull(createdVictim);
        assertEquals("John", createdVictim.firstName());
        assertEquals("Doe", createdVictim.lastName());
        assertEquals("Company", createdVictim.workplace());
        assertEquals("Manager", createdVictim.position());
        assertEquals("New York", createdVictim.residence());
        assertEquals("555-1234", createdVictim.phone());
        assertEquals("Injured", createdVictim.description());
    }

    @Test
    void testGetVictim() {
        CreateVictimRequest request = new CreateVictimRequest(
                "Jane", "Smith", "Corporation", "Engineer", "Chicago", "555-5678", "Severely Injured");
        VictimDto createdVictim = victimService.createVictim(request);

        VictimDto fetchedVictim = victimService.getVictim(createdVictim.id());

        assertNotNull(fetchedVictim);
        assertEquals("Jane", fetchedVictim.firstName());
        assertEquals("Smith", fetchedVictim.lastName());
    }

    @Test
    void testGetAllVictims() {
        victimService.createVictim(new CreateVictimRequest("John", "Doe", "Company", "Manager", "New York", "555-1234", "Injured"));
        victimService.createVictim(new CreateVictimRequest("Jane", "Smith", "Corporation", "Engineer", "Chicago", "555-5678", "Severely Injured"));

        Page<VictimDto> victims = victimService.getAllVictims(0, 10);

        assertNotNull(victims);
        assertEquals(2, victims.getTotalElements());
    }

    @Test
    void testDeleteVictim() {
        VictimDto createdVictim = victimService.createVictim(new CreateVictimRequest(
                "John", "Doe", "Company", "Manager", "New York", "555-1234", "Injured"));

        victimService.deleteVictim(createdVictim.id());

        assertThrows(EntityNotFoundException.class, () -> victimService.getVictim(createdVictim.id()));
    }
}
