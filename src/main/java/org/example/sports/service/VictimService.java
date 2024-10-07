package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
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

    public Victim createVictim(CreateVictimRequest request) {
        Victim victim = Victim.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .workplace(request.workplace())
                .position(request.position())
                .residence(request.residence())
                .phone(request.phone())
                .description(request.description())
                .build();

        return victimRepository.save(victim);
    }

    public Victim getVictimById(Long id) {
        return victimRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Victim not found"));
    }

    public Page<Victim> getAllVictims(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return victimRepository.findAll(pageable);
    }

    public void deleteVictim(Long id) {
        victimRepository.deleteById(id);
    }
}
