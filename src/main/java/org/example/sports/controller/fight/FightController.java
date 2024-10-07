package org.example.sports.controller.fight;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.fight.dto.CreateFight;
import org.example.sports.controller.fight.dto.FightDto;
import org.example.sports.mapper.FightMapper;
import org.example.sports.model.Fight;
import org.example.sports.model.enums.FightStatus;
import org.example.sports.service.FightService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports/user/fight")
@RequiredArgsConstructor
public class FightController {

    private final FightService fightService;
    private final FightMapper fightMapper;

    @GetMapping("/{executorId}")
    public ResponseEntity<Page<FightDto>> getFightsByExecutorId(
            @PathVariable String executorId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        if (size > 50) {size = 50;}
        Page<FightDto> fights = fightService.getFightsByExecutorId(executorId, page, size)
                .map(fightMapper::convertToDto);
        return ResponseEntity.ok(fights);
    }


    @PostMapping
    public ResponseEntity<FightDto> createFight(@Valid @RequestBody CreateFight fightDto) {
        Fight createdFight = fightService.createFight(fightMapper.convert(fightDto));
        return new ResponseEntity<>(fightMapper.convertToDto(createdFight), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<FightDto> updateFightStatus(
            @PathVariable Long id,
            @RequestParam FightStatus newStatus) {
        Fight updatedFight = fightService.updateFightStatus(id, newStatus);
        return ResponseEntity.ok(fightMapper.convertToDto(updatedFight));
    }
}
