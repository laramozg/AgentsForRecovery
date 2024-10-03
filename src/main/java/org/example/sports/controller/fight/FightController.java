package org.example.sports.controller.fight;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.fight.dto.CreateFight;
import org.example.sports.controller.fight.dto.FightDto;
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

    @GetMapping("/{executorId}")
    public ResponseEntity<Page<FightDto>> getFightsByExecutorId(
            @PathVariable String executorId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        if (size > 50) {size = 50;}
        Page<FightDto> fights = fightService.getFightsByExecutorId(executorId, page, size);
        return ResponseEntity.ok(fights);
    }


    @PostMapping
    public ResponseEntity<FightDto> createFight(@Valid @RequestBody CreateFight fightDto) {
        FightDto createdFight = fightService.createFight(fightDto);
        return new ResponseEntity<>(createdFight, HttpStatus.CREATED);
    }
}
