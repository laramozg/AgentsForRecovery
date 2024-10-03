package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
import org.example.sports.controller.victim.dto.VictimDto;
import org.example.sports.mapper.VictimMapper;
import org.example.sports.model.Victim;
import org.example.sports.repositore.VictimRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VictimService {

    private final VictimRepository victimRepository;
    private final VictimMapper victimMapper;

    public VictimDto createVictim(CreateVictimRequest request) {
        Victim victim = Victim.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .workplace(request.workplace())
                .position(request.position())
                .residence(request.residence())
                .phone(request.phone())
                .description(request.description())
                .build();

        Victim savedVictim = victimRepository.save(victim);
        return victimMapper.toDto(savedVictim);
    }

    public VictimDto getVictim(Long id) {
        return victimRepository.findById(id)
                .map(victimMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Victim not found"));
    }

    public Page<VictimDto> getAllVictims(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Victim> victims = victimRepository.findAll(pageable);
        return victims.map(victimMapper::toDto);
    }

    public void deleteVictim(Long id) {
        victimRepository.deleteById(id);
    }
}
