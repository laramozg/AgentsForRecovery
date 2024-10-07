package org.example.sports.mapper;

import lombok.AllArgsConstructor;
import org.example.sports.controller.fight.dto.CreateFight;
import org.example.sports.controller.fight.dto.FightDto;
import org.example.sports.model.Fight;
import org.example.sports.service.ExecutorService;
import org.example.sports.service.OrderService;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FightMapper {
    private final ExecutorService executorService;
    private final OrderService orderService;

    public Fight convert(CreateFight createFight){
        return Fight.builder()
                .executor(executorService.getExecutorById(createFight.executorId()))
                .order(orderService.getOrderById(createFight.orderId()))
                .build();
    }
    public FightDto convertToDto(Fight fight) {
        return new FightDto(
                fight.getId(),
                fight.getExecutor().getUsername(),
                fight.getOrder().getId(),
                fight.getStatus().name()
        );
    }
}
