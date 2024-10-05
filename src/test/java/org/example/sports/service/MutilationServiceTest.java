package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.sports.controller.mutilation.dto.CreateMutilation;
import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.repositore.MutilationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MutilationServiceTest {

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
    private MutilationService mutilationService;

    @Autowired
    private MutilationRepository mutilationRepository;

    @BeforeEach
    void setUp() {
        mutilationRepository.deleteAll();
    }

    @Test
    void testCreateMutilation() {
        CreateMutilation createMutilation = new CreateMutilation("Type", 1000);

        MutilationDto createdMutilation = mutilationService.createMutilation(createMutilation);

        assertNotNull(createdMutilation);
        assertEquals("Type", createdMutilation.type());
        assertEquals(1000, createdMutilation.price());
    }

    @Test
    void testFindAllMutilations() {
        mutilationService.createMutilation(new CreateMutilation("Type A", 1000));
        mutilationService.createMutilation(new CreateMutilation("Type B", 2000));

        List<MutilationDto> mutilations = mutilationService.findAllMutilations(0, 10);

        assertNotNull(mutilations);
        assertEquals(2, mutilations.size());
    }

    @Test
    void testFindMutilationById() {
        MutilationDto createdMutilation = mutilationService.createMutilation(new CreateMutilation("Type A", 1000));

        MutilationDto foundMutilation = mutilationService.findMutilationById(createdMutilation.id());

        assertNotNull(foundMutilation);
        assertEquals("Type A", foundMutilation.type());
    }

    @Test
    void testUpdateMutilation() {
        MutilationDto createdMutilation = mutilationService.createMutilation(new CreateMutilation("Type A", 1000));

        CreateMutilation updatedMutilation = new CreateMutilation("Type B", 1500);
        MutilationDto updatedMutilationDto = mutilationService.updateMutilation(createdMutilation.id(), updatedMutilation);

        assertNotNull(updatedMutilationDto);
        assertEquals("Type B", updatedMutilationDto.type());
        assertEquals(1500, updatedMutilationDto.price());
    }

    @Test
    void testDeleteMutilation() {
        MutilationDto createdMutilation = mutilationService.createMutilation(new CreateMutilation("Type A", 1000));

        mutilationService.deleteMutilation(createdMutilation.id());

        assertThrows(EntityNotFoundException.class, () -> mutilationService.findMutilationById(createdMutilation.id()));
    }
}
