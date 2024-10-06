package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.mutilation.dto.CreateMutilation;
import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.model.Mutilation;
import org.example.sports.repositore.MutilationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MutilationServiceTest extends AbstractServiceTest {

    @Autowired
    private MutilationService mutilationService;

    @Autowired
    private MutilationRepository mutilationRepository;

    private Mutilation mutilation;

    @BeforeEach
    void setUp() {
        mutilation = buildCreateMutilation("Type1", 1000);
    }

    @AfterEach
    void tearDown() {
        mutilationRepository.deleteAll();
    }

    private Mutilation buildCreateMutilation(String type, int price) {
        return mutilationRepository.save( Mutilation.builder().type(type).price(price).build());
    }

    @Test
    void createMutilationSuccessfully() {
        MutilationDto createdMutilation = mutilationService.createMutilation(
                new CreateMutilation("Type1", 1000));

        assertNotNull(createdMutilation);
        assertEquals("Type1", createdMutilation.type());
        assertEquals(1000, createdMutilation.price());
    }

    @Test
    void findAllMutilationsSuccessfully() {
        buildCreateMutilation("Type2", 2000);

        List<MutilationDto> mutilations = mutilationService.findAllMutilations(0, 10);

        assertNotNull(mutilations);
        assertEquals(2, mutilations.size());
    }

    @Test
    void findMutilationByIdSuccessfully() {
        MutilationDto foundMutilation = mutilationService.findMutilationById(mutilation.getId());

        assertNotNull(foundMutilation);
        assertEquals("Type1", foundMutilation.type());
    }

    @Test
    void updateMutilationSuccessfully() {

        MutilationDto updatedMutilationDto = mutilationService.updateMutilation(mutilation.getId(),
                new CreateMutilation("Type2", 2000));

        assertNotNull(updatedMutilationDto);
        assertEquals("Type2", updatedMutilationDto.type());
        assertEquals(2000, updatedMutilationDto.price());
    }

    @Test
    void deleteMutilationSuccessfully() {
        mutilationService.deleteMutilation(mutilation.getId());

        assertThrows(EntityNotFoundException.class, () -> mutilationService.findMutilationById(mutilation.getId()));
    }
}
