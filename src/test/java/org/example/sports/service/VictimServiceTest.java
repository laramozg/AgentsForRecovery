package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
import org.example.sports.model.Victim;
import org.example.sports.repositore.VictimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.example.sports.util.Models.VICTIM;
import static org.example.sports.util.Models.CREATE_VICTIM_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VictimServiceTest {

    @Mock
    private VictimRepository victimRepository;

    @InjectMocks
    private VictimService victimService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createVictim_ShouldCreateAndReturnVictim() {
        CreateVictimRequest request = CREATE_VICTIM_REQUEST();

        when(victimRepository.save(any(Victim.class))).thenReturn(VICTIM());

        Victim createdVictim = victimService.createVictim(request);

        assertNotNull(createdVictim);
        assertEquals(request.firstName(), createdVictim.getFirstName());
        assertEquals(request.lastName(), createdVictim.getLastName());
        assertEquals(request.workplace(), createdVictim.getWorkplace());
    }

    @Test
    void getVictimById_ShouldReturnVictimWhenFound() {
        Victim expectedVictim = VICTIM();
        when(victimRepository.findById(anyLong())).thenReturn(Optional.of(expectedVictim));

        Victim foundVictim = victimService.getVictimById(expectedVictim.getId());

        assertNotNull(foundVictim);
        assertEquals(expectedVictim.getId(), foundVictim.getId());
    }

    @Test
    void getVictimById_ShouldThrowExceptionWhenNotFound() {
        when(victimRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                victimService.getVictimById(1L));

        assertEquals("Victim not found", exception.getMessage());
    }

    @Test
    void getAllVictims_ShouldReturnPageOfVictims() {
        Victim victim = VICTIM();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Victim> victimPage = new PageImpl<>(Collections.singletonList(victim), pageable, 1);

        when(victimRepository.findAll(any(Pageable.class))).thenReturn(victimPage);

        Page<Victim> result = victimService.getAllVictims(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(victim, result.getContent().get(0));
    }

    @Test
    void deleteVictim_ShouldDeleteVictim() {
        doNothing().when(victimRepository).deleteById(anyLong());

        victimService.deleteVictim(1L);

        verify(victimRepository, times(1)).deleteById(1L);
    }
}
