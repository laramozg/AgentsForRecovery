package org.example.sports.mapper;

import org.example.sports.controller.victim.dto.VictimDto;
import org.example.sports.model.Victim;
import org.springframework.stereotype.Component;

@Component
public class VictimMapper {

    public VictimDto toDto(Victim victim) {
        return new VictimDto(
                victim.getId(),
                victim.getFirstName(),
                victim.getLastName(),
                victim.getWorkplace(),
                victim.getPosition(),
                victim.getResidence(),
                victim.getPhone(),
                victim.getDescription());
    }
}
