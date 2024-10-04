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

        Fight createFight = fightRepository.save(fight);
        return fightMapper.toFightDto(createFight);
    }

}
