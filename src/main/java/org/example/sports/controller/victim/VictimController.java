package org.example.sports.controller.victim;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.victim.dto.CreateVictimRequest;
import org.example.sports.controller.victim.dto.VictimDto;
import org.example.sports.mapper.VictimMapper;
import org.example.sports.service.VictimService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports/user/victim")
@RequiredArgsConstructor
public class VictimController {
    private final VictimService victimService;
    private final VictimMapper victimMapper;

    @PostMapping
    public ResponseEntity<VictimDto> createVictim(@Valid @RequestBody CreateVictimRequest request) {
        return new ResponseEntity<>(victimMapper.convertToDto(victimService.createVictim(request)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VictimDto> getVictim(@PathVariable Long id) {
        return ResponseEntity.ok(victimMapper.convertToDto(victimService.getVictimById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<VictimDto>> getAllVictims(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        if (size > 50) {size = 50;}
        Page<VictimDto> victims = victimService.getAllVictims(page, size).map(victimMapper::convertToDto);
        return ResponseEntity.ok(victims);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVictim(@PathVariable Long id) {
        victimService.deleteVictim(id);
        return ResponseEntity.noContent().build();
    }
}
