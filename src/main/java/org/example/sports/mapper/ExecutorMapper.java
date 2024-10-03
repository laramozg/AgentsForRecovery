package org.example.sports.mapper;

import org.example.sports.controller.executor.dto.ExecutorDto;
import org.example.sports.model.Executor;
import org.springframework.stereotype.Component;

@Component
public class ExecutorMapper {
    public ExecutorDto toDto(Executor executor) {
        return new ExecutorDto(
                executor.getUsername(),
                executor.getPassportSeriesNumber(),
                executor.getWeight(),
                executor.getHeight(),
                executor.getRating(),
                executor.getCompletedOrders());
    }
}
