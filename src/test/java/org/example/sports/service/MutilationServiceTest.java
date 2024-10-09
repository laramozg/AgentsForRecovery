package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.sports.controller.mutilation.dto.MutilationRequest;
import org.example.sports.model.Mutilation;
import org.example.sports.repositore.MutilationRepository;
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
import java.util.List;
import java.util.Optional;

import static org.example.sports.util.Models.CREATE_MUTILATION_REQUEST;
import static org.example.sports.util.Models.MUTILATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MutilationServiceTest {

    @Mock
    private MutilationRepository mutilationRepository;

    @InjectMocks
    private MutilationService mutilationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMutilation_ShouldCreateAndReturnMutilation() {
        MutilationRequest request = CREATE_MUTILATION_REQUEST();

        when(mutilationRepository.save(any(Mutilation.class))).thenReturn(MUTILATION());

        Mutilation createdMutilation = mutilationService.createMutilation(request);

        assertNotNull(createdMutilation);
        assertEquals(request.type(), createdMutilation.getType());
        assertEquals(request.price(), createdMutilation.getPrice());
    }

    @Test
    void findAllMutilations_ShouldReturnPageOfMutilations() {
        Mutilation mutilation = MUTILATION();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Mutilation> mutilationPage = new PageImpl<>(Collections.singletonList(mutilation), pageable, 1);

        when(mutilationRepository.findAll(any(Pageable.class))).thenReturn(mutilationPage);

        Page<Mutilation> result = mutilationService.findAllMutilations(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(mutilation, result.getContent().get(0));
    }

    @Test
    void findMutilationById_ShouldReturnMutilationWhenFound() {
        Mutilation expectedMutilation = MUTILATION();
        when(mutilationRepository.findById(anyLong())).thenReturn(Optional.of(expectedMutilation));

        Mutilation foundMutilation = mutilationService.findMutilationById(expectedMutilation.getId());

        assertNotNull(foundMutilation);
        assertEquals(expectedMutilation.getId(), foundMutilation.getId());

    }

    @Test
    void findMutilationById_ShouldThrowExceptionWhenNotFound() {
        when(mutilationRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                mutilationService.findMutilationById(1L));

        assertEquals("Mutilation not found", exception.getMessage());
    }

    @Test
    void deleteMutilation_ShouldDeleteMutilation() {
        doNothing().when(mutilationRepository).deleteById(anyLong());

        mutilationService.deleteMutilation(1L);

        verify(mutilationRepository, times(1)).deleteById(1L);
    }

    @Test
    void findAllMutilationsById_ShouldReturnListOfMutilationsWhenAllFound() {
        List<Long> mutilationIds = List.of(1L);
        Mutilation mutilation = MUTILATION();
        when(mutilationRepository.findAllByIdIn(mutilationIds)).thenReturn(Collections.singletonList(mutilation));

        List<Mutilation> result = mutilationService.findAllMutilationsById(mutilationIds);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(mutilation));
    }

    @Test
    void findAllMutilationsById_ShouldThrowExceptionWhenSomeNotFound() {
        List<Long> mutilationIds = List.of(1L, 2L);
        when(mutilationRepository.findAllById(mutilationIds)).thenReturn(Collections.singletonList(MUTILATION()));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                mutilationService.findAllMutilationsById(mutilationIds));

        assertEquals("Some mutilations not found", exception.getMessage());
    }

    @Test
    void updateMutilation_ShouldUpdateAndReturnMutilation() {
        MutilationRequest request = CREATE_MUTILATION_REQUEST();
        Mutilation existingMutilation = MUTILATION();

        when(mutilationRepository.findById(anyLong())).thenReturn(Optional.of(existingMutilation));
        when(mutilationRepository.save(any(Mutilation.class))).thenReturn(existingMutilation);

        Mutilation updatedMutilation = mutilationService.updateMutilation(1L, request);

        assertNotNull(updatedMutilation);
        assertEquals(request.type(), updatedMutilation.getType());
        assertEquals(request.price(), updatedMutilation.getPrice());
    }

    @Test
    void updateMutilation_ShouldThrowExceptionWhenNotFound() {
        when(mutilationRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                mutilationService.updateMutilation(1L, CREATE_MUTILATION_REQUEST()));

        assertEquals("Mutilation not found", exception.getMessage());
    }
}
