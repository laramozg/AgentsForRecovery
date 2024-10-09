package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.mutilation.dto.MutilationRequest;
import org.example.sports.model.Mutilation;
import org.example.sports.repositore.MutilationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class MutilationService {

    private final MutilationRepository mutilationRepository;

    public Mutilation createMutilation(MutilationRequest mutilationDto) {
        Mutilation mutilation = Mutilation.builder()
                .type(mutilationDto.type())
                .price(mutilationDto.price())
                .build();

        return mutilationRepository.save(mutilation);
    }

    public Page<Mutilation> findAllMutilations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mutilationRepository.findAll(pageable);
    }

    public Mutilation findMutilationById(Long id) {
        return mutilationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mutilation not found"));
    }

    public void deleteMutilation(Long id) {
        mutilationRepository.deleteById(id);
    }

    public List<Mutilation> findAllMutilationsById(List<Long> mutilationIds) {
        List<Mutilation> mutilations = mutilationRepository.findAllByIdIn(mutilationIds);
        if (mutilations.size() != mutilationIds.size()) {
            throw new EntityNotFoundException("Some mutilations not found");
        }
        return mutilations;
    }

    public Mutilation updateMutilation(Long id, MutilationRequest mutilationDto) {
        Mutilation mutilation = findMutilationById(id);

        mutilation.setType(mutilationDto.type());
        mutilation.setPrice(mutilationDto.price());
        return mutilationRepository.save(mutilation);
    }
}
