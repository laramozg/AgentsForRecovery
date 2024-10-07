package org.example.sports.mapper;

import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.model.Mutilation;
import org.springframework.stereotype.Component;

@Component
public class MutilationMapper {

    public MutilationDto convertToDto(Mutilation mutilation) {
        return new MutilationDto(
                mutilation.getId(),
                mutilation.getType(),
                mutilation.getPrice());
    }
}
