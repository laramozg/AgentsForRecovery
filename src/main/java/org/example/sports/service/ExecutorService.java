package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.executor.dto.ExecutorDto;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.mapper.ExecutorMapper;
import org.example.sports.model.Executor;
import org.example.sports.model.User;
import org.example.sports.repositore.ExecutorRepository;
import org.example.sports.repositore.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExecutorService {
    private final ExecutorRepository executorRepository;
    private final UserRepository userRepository;
    private final ExecutorMapper executorMapper;


    public ExecutorDto createExecutor(ExecutorRequest executorDto) {
        User user = userRepository.findById(executorDto.username())
                .orElseThrow(() -> new EntityNotFoundException("User " + executorDto.username() + " not found"));

        Executor executor = Executor.builder()
                .username(executorDto.username())
                .passportSeriesNumber(executorDto.passportSeriesNumber())
                .weight(executorDto.weight())
                .height(executorDto.height())
                .rating(0.0)
                .completedOrders(0)
                .user(user)
                .build();
        Executor createdExecutor = executorRepository.save(executor);
        return executorMapper.toDto(createdExecutor);
    }

    public ExecutorDto getUser(String username) {
        return executorRepository.findById(username)
                .map(executorMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + username + "' not found"));
    }


    public ExecutorDto updateUser(String username, ExecutorRequest executorDto) {
        Executor executor = executorRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + username + "' not found"));

        executor.setPassportSeriesNumber(executorDto.passportSeriesNumber());
        executor.setWeight(executorDto.weight());
        executor.setHeight(executorDto.height());
        Executor updatedExecutor = executorRepository.save(executor);
        return executorMapper.toDto(updatedExecutor);
    }

}
