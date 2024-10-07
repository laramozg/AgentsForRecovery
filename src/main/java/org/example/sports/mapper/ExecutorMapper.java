package org.example.sports.mapper;

import lombok.AllArgsConstructor;
import org.example.sports.controller.executor.dto.ExecutorDto;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.model.Executor;
import org.example.sports.service.UserService;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ExecutorMapper {
    private final UserService userService;

    public ExecutorDto convertToDto(Executor executor) {
        return new ExecutorDto(
                executor.getUsername(),
                executor.getPassportSeriesNumber(),
                executor.getWeight(),
                executor.getHeight(),
                executor.getRating(),
                executor.getCompletedOrders());
    }

    public Executor convert(ExecutorRequest executorRequest) {
        return Executor.builder()
                .user(userService.getUserById(executorRequest.username()))
                .username(executorRequest.username())
                .passportSeriesNumber(executorRequest.passportSeriesNumber())
                .weight(executorRequest.weight())
                .height(executorRequest.height())
                .build();
    }
}
