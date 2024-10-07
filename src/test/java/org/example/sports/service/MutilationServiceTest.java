package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.mutilation.dto.MutilationRequest;
import org.example.sports.model.Mutilation;
import org.example.sports.repositore.MutilationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

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
        Mutilation createdMutilation = mutilationService.createMutilation(
                new MutilationRequest("Type1", 1000));

        assertNotNull(createdMutilation);
        assertEquals("Type1", createdMutilation.getType());
        assertEquals(1000, createdMutilation.getPrice());
    }

    @Test
    void findAllMutilationsSuccessfully() {
        buildCreateMutilation("Type2", 2000);

        Page<Mutilation> mutilations = mutilationService.findAllMutilations(0, 10);

        assertNotNull(mutilations);
        assertEquals(2, mutilations.getTotalElements());
    }

    @Test
    void findMutilationByIdSuccessfully() {
        Mutilation foundMutilation = mutilationService.findMutilationById(mutilation.getId());

        assertNotNull(foundMutilation);
        assertEquals("Type1", foundMutilation.getType());
    }

    @Test
    void updateMutilationSuccessfully() {

        Mutilation updatedMutilationDto = mutilationService.updateMutilation(mutilation.getId(),
                new MutilationRequest("Type2", 2000));

        assertNotNull(updatedMutilationDto);
        assertEquals("Type2", updatedMutilationDto.getType());
        assertEquals(2000, updatedMutilationDto.getPrice());
    }

    @Test
    void deleteMutilationSuccessfully() {
        mutilationService.deleteMutilation(mutilation.getId());

        assertThrows(EntityNotFoundException.class, () -> mutilationService.findMutilationById(mutilation.getId()));
    }

    @Test
    void testFindAllMutilationsById_Success() {
        Mutilation mutilation1 = mutilationRepository.save(Mutilation.builder().type("Type1").price(1000).build());
        Mutilation mutilation2 = mutilationRepository.save(Mutilation.builder().type("Type2").price(2000).build());

        Set<Long> ids = Set.of(mutilation1.getId(), mutilation2.getId());
        Set<Mutilation> mutilations = mutilationService.findAllMutilationsById(ids);

        assertEquals(2, mutilations.size());
    }
}
