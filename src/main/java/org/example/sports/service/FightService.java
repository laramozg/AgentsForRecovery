package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.fight.dto.CreateFight;
import org.example.sports.controller.fight.dto.FightDto;
import org.example.sports.mapper.FightMapper;
import org.example.sports.model.Executor;
import org.example.sports.model.Fight;
import org.example.sports.model.Order;
import org.example.sports.model.enums.FightStatus;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.repositore.ExecutorRepository;
import org.example.sports.repositore.FightRepository;
import org.example.sports.repositore.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FightService {
    private final FightRepository fightRepository;
    private final FightMapper fightMapper;
    private final ExecutorRepository executorRepository;
    private final OrderRepository orderRepository;


    public Page<FightDto> getFightsByExecutorId(String executorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Fight> fights = fightRepository.findByExecutor_Username(executorId, pageable);
        return fights.map(fightMapper::toFightDto);
    }

    @Transactional
    public FightDto createFight(CreateFight fightDto) {
        Executor executor = executorRepository.findById(fightDto.executorId())
                .orElseThrow(() -> new EntityNotFoundException("Executor not found"));
        Order order = orderRepository.findById(fightDto.orderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        Fight fight = Fight.builder()
                .executor(executor)
                .order(order)
                .status(FightStatus.PENDING)
                .build();

        order.setStatus(OrderStatus.PERFORMANCE);
        Fight createFight = fightRepository.save(fight);
        return fightMapper.toFightDto(createFight);
    }


    @Transactional
    public FightDto updateFightStatus(Long fightId, FightStatus newStatus) {
        Fight fight = fightRepository.findById(fightId)
                .orElseThrow(() -> new EntityNotFoundException("Fight not found"));
        Order order = orderRepository.findById(fight.getOrder().getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        Executor executor = executorRepository.findById(fight.getExecutor().getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Executor not found"));


        fight.setStatus(newStatus);

        Fight updatedFight = fightRepository.save(fight);
        if (newStatus == FightStatus.VICTORY) {
            order.setStatus(OrderStatus.DONE);
            executor.setCompletedOrders(executor.getCompletedOrders() + 1);
        }else {
            order.setStatus(OrderStatus.WAITING);
        }

        orderRepository.save(order);
        updateExecutorRating(executor);
        return fightMapper.toFightDto(updatedFight);
    }

    private void updateExecutorRating(Executor executor) {
        long victoriesCount = fightRepository.countByExecutorUsernameAndStatus(executor.getUsername(), FightStatus.VICTORY);
        long totalFightsCount = fightRepository.countByExecutorUsername(executor.getUsername());

        if (totalFightsCount > 0) {
            double rating = (double) victoriesCount / totalFightsCount * 10;
            executor.setRating(rating);
        } else {
            executor.setRating(0.0);
        }

        executorRepository.save(executor);
    }
}
