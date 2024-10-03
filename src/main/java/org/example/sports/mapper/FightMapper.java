package org.example.sports.mapper;

import org.example.sports.controller.fight.dto.FightDto;
import org.example.sports.model.Fight;
import org.springframework.stereotype.Component;

@Component
public class FightMapper {

    public FightDto toFightDto(Fight fight) {
        return new FightDto(
                fight.getId(),
                fight.getExecutor().getUsername(),
                fight.getOrder().getId(),
                fight.getStatus().name()
        );
    }
}
