package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
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

        Victim createdVictim = victimService.createVictim(request);

        assertNotNull(createdVictim);
        assertEquals("John", createdVictim.getFirstName());
        assertEquals("Doe", createdVictim.getLastName());
        assertEquals("Company", createdVictim.getWorkplace());
        assertEquals("Manager", createdVictim.getPosition());
        assertEquals("New York", createdVictim.getResidence());
        assertEquals("555-1234", createdVictim.getPhone());
        assertEquals("Injured", createdVictim.getDescription());
    }

    @Test
    void getVictimSuccessfully() {
        Victim victim = buildCreateVictim("Jane", "Smith", "Corporation",
                "Engineer", "Chicago", "555-5678", "Severely Injured");

        Victim fetchedVictim = victimService.getVictimById(victim.getId());

        assertNotNull(fetchedVictim);
        assertEquals("Jane", fetchedVictim.getFirstName());
        assertEquals("Smith", fetchedVictim.getLastName());
    }

    @Test
    void getAllVictimsSuccessfully() {
        buildCreateVictim("John", "Doe", "Company",
                "Manager", "New York", "555-1234", "Injured");
        buildCreateVictim("Jane", "Smith", "Corporation",
                "Engineer", "Chicago", "555-5678", "Severely Injured");

        Page<Victim> victims = victimService.getAllVictims(0, 10);

        assertNotNull(victims);
        assertEquals(2, victims.getTotalElements());
    }

    @Test
    void deleteVictimSuccessfully() {
        Victim victim = buildCreateVictim("John", "Doe", "Company",
                "Manager", "New York", "555-1234", "Injured");

        victimService.deleteVictim(victim.getId());

        assertThrows(EntityNotFoundException.class, () -> victimService.getVictimById(victim.getId()));
    }
}
