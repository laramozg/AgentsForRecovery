package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
import org.example.sports.controller.victim.dto.VictimDto;
import org.example.sports.model.Victim;
import org.example.sports.repositore.VictimRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class VictimServiceTest extends AbstractServiceTest {

    @Autowired
    private VictimService victimService;

    @Autowired
    private VictimRepository victimRepository;

    @AfterEach
    void tearDown() {
        victimRepository.deleteAll();
    }


    private Victim buildCreateVictim(String firstName, String lastName, String workplace, String position,
                                     String residence, String phone, String description) {
        return victimRepository.save(Victim.builder().firstName(firstName).lastName(lastName).workplace(workplace)
                .position(position).residence(residence).phone(phone).description(description).build());
    }

    @Test
    void createVictimSuccessfully() {
        CreateVictimRequest request = new CreateVictimRequest("John", "Doe", "Company",
                "Manager", "New York", "555-1234", "Injured");

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
    void getVictimSuccessfully() {
        Victim victim = buildCreateVictim("Jane", "Smith", "Corporation",
                "Engineer", "Chicago", "555-5678", "Severely Injured");

        VictimDto fetchedVictim = victimService.getVictim(victim.getId());

        assertNotNull(fetchedVictim);
        assertEquals("Jane", fetchedVictim.firstName());
        assertEquals("Smith", fetchedVictim.lastName());
    }

    @Test
    void getAllVictimsSuccessfully() {
        buildCreateVictim("John", "Doe", "Company",
                "Manager", "New York", "555-1234", "Injured");
        buildCreateVictim("Jane", "Smith", "Corporation",
                "Engineer", "Chicago", "555-5678", "Severely Injured");

        Page<VictimDto> victims = victimService.getAllVictims(0, 10);

        assertNotNull(victims);
        assertEquals(2, victims.getTotalElements());
    }

    @Test
    void deleteVictimSuccessfully() {
        Victim victim = buildCreateVictim("John", "Doe", "Company",
                "Manager", "New York", "555-1234", "Injured");

        victimService.deleteVictim(victim.getId());

        assertThrows(EntityNotFoundException.class, () -> victimService.getVictim(victim.getId()));
    }
}
