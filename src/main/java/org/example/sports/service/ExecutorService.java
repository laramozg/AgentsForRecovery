package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.model.Executor;
import org.example.sports.repositore.ExecutorRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExecutorService {
    private final ExecutorRepository executorRepository;

    public Executor createExecutor(Executor executor) {
        executor.setCompletedOrders(0);
        executor.setRating(0.0);
        return executorRepository.save(executor);
    }

    public Executor getExecutorById(String username) {
        return executorRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + username + "' not found"));
    }

    @Transactional
    public Executor updateExecutor(ExecutorRequest executorRequest) {
        Executor executor = getExecutorById(executorRequest.username());
        executor.setPassportSeriesNumber(executorRequest.passportSeriesNumber());
        executor.setWeight(executorRequest.weight());
        executor.setHeight(executorRequest.height());
        return executorRepository.save(executor);
    }

}
