package org.example.sports.service;

import lombok.RequiredArgsConstructor;
import org.example.sports.model.entity.Mutilation;
import org.example.sports.repositore.MutilationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MutilationService {

    private final MutilationRepository mutilationRepo;

    public List<Mutilation> findAllMutilation() {
        return mutilationRepo.findAll();
    }
}
