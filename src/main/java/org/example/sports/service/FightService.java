package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sports.model.Executor;
import org.example.sports.model.Fight;
import org.example.sports.model.Order;
import org.example.sports.model.enums.FightStatus;
import org.example.sports.model.enums.OrderStatus;
import org.example.sports.repositore.FightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FightService {
    private final OrderService orderService;
    private final ExecutorService executorService;
    private final FightRepository fightRepository;

    public Page<Fight> getFightsByExecutorId(String executorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fightRepository.findByExecutor_Username(executorId, pageable);
    }

    public Fight getFightById(Long id) {
        return fightRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fight not found"));
    }

    @Transactional
    public Fight createFight(Fight fight) {
        fight.setStatus(FightStatus.PENDING);
        Order order = orderService.getOrderById(fight.getOrder().getId());
        order.setStatus(OrderStatus.PERFORMANCE);

        return fightRepository.save(fight);
    }


    @Transactional
    public Fight updateFightStatus(Long fightId, FightStatus newStatus) {
        Fight fight = getFightById(fightId);

        Order order = orderService.getOrderById(fight.getOrder().getId());
        Executor executor = executorService.getExecutorById(fight.getExecutor().getUsername());

        fight.setStatus(newStatus);

        Fight updatedFight = fightRepository.save(fight);
        if (newStatus == FightStatus.VICTORY) {
            order.setStatus(OrderStatus.DONE);
            executor.setCompletedOrders(executor.getCompletedOrders() + 1);
        } else {
            order.setStatus(OrderStatus.WAITING);
        }
        updateExecutorRating(executor);
        return updatedFight;
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
    }
}
