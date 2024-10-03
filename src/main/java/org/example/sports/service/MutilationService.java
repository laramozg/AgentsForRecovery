package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.mutilation.dto.CreateMutilation;
import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.mapper.MutilationMapper;
import org.example.sports.model.Mutilation;
import org.example.sports.repositore.MutilationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MutilationService {

    private final MutilationRepository mutilationRepository;
    private final MutilationMapper mutilationMapper;

    public MutilationDto createMutilation(CreateMutilation mutilationDto) {
        Mutilation mutilation = Mutilation.builder()
                .type(mutilationDto.type())
                .price(mutilationDto.price())
                .build();

        Mutilation createMut = mutilationRepository.save(mutilation);
        return mutilationMapper.toDto(createMut);
    }

    public List<MutilationDto> findAllMutilations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Mutilation> mutilations = mutilationRepository.findAll(pageable);
        return mutilations.stream()
                .map(mutilationMapper::toDto)
                .toList();
    }

    public MutilationDto findMutilationById(Long id) {
        return mutilationRepository.findById(id)
                .map(mutilationMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Mutilation not found"));
    }

    public void deleteMutilation(Long id) {
        mutilationRepository.deleteById(id);
    }

    public MutilationDto updateMutilation(Long id, CreateMutilation mutilationDto) {
        Mutilation mutilation = mutilationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mutilation not found"));

        mutilation.setType(mutilationDto.type());
        mutilation.setPrice(mutilationDto.price());
        Mutilation updateMut = mutilationRepository.save(mutilation);
        return mutilationMapper.toDto(updateMut);
    }
}
