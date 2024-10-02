package org.example.sports.service;

import lombok.RequiredArgsConstructor;
import org.example.sports.exceptions.BadRequestException;
import org.example.sports.exceptions.UserNotFoundException;
import org.example.sports.model.dto.ExecutorDto;
import org.example.sports.model.dto.UserDto;
import org.example.sports.model.entity.Executor;
import org.example.sports.model.entity.User;
import org.example.sports.repositore.AuthorizationRepository;
import org.example.sports.repositore.ExecutorRepository;
import org.example.sports.repositore.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExecutorService {
    private final ExecutorRepository executorRepository;
    private final UserRepository userRepository;

    public ExecutorDto createExecutor(ExecutorDto executorDto) {
        if (executorDto.username() == null || executorDto.passportSeriesNumber() == null) {
            throw new BadRequestException("Username and passport series number must be filled.");
        }

        User user = userRepository.findById(executorDto.username())
                .orElseThrow(() -> new UserNotFoundException("User with username '" + executorDto.username() + "' not found"));

        Executor executor = Executor.builder()
                .username(executorDto.username())
                .passportSeriesNumber(executorDto.passportSeriesNumber())
                .weight(executorDto.weight())
                .height(executorDto.height())
                .rating(executorDto.rating())
                .completedOrders(executorDto.completedOrders())
                .user(user)
                .build();
        System.out.println(executor.getUsername());
        System.out.println(user.getAuthorizationData().getUsername());

        executorRepository.save(executor);
        return executorDto;
    }

    public ExecutorDto getUser(String username) {
        return executorRepository.findById(username)
                .map(executor -> new ExecutorDto(executor.getUsername(), executor.getPassportSeriesNumber(),
                        executor.getWeight(), executor.getHeight(), executor.getRating(), executor.getCompletedOrders()))
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
    }


    public ExecutorDto updateUser(String username, ExecutorDto executorDto) {
        Executor executor = executorRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        if (executorDto.passportSeriesNumber() == null || executorDto.weight() == null ||
                executorDto.completedOrders() == null || executorDto.height() == null || executorDto.rating() == null) {
            throw new BadRequestException("Nick and Telegram must not be null.");
        }

        executor.setPassportSeriesNumber(executorDto.passportSeriesNumber());
        executor.setWeight(executorDto.weight());
        executor.setHeight(executorDto.height());
        executor.setRating(executorDto.rating());
        executor.setCompletedOrders(executorDto.completedOrders());

        executorRepository.save(executor);
        return executorDto;
    }
}
