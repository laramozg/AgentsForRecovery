package org.example.sports.controller.mutilation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.mutilation.dto.MutilationRequest;
import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.mapper.MutilationMapper;
import org.example.sports.model.Mutilation;
import org.example.sports.service.MutilationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sports/user/mutilation")
@RequiredArgsConstructor
public class MutilationController {
    private final MutilationService mutilationService;
    private final MutilationMapper mutilationMapper;

    @PostMapping("/change")
    public ResponseEntity<MutilationDto> createMutilation(
            @Valid @RequestBody MutilationRequest mutilationRequest) {
        Mutilation mutilation = mutilationService.createMutilation(mutilationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(mutilationMapper.convertToDto(mutilation));
    }

    @GetMapping
    public ResponseEntity<List<MutilationDto>> getAllMutilations(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        if (size > 50) {
            size = 50;
        }
        List<MutilationDto> mutilations = mutilationService.findAllMutilations(page, size).stream()
                .map(mutilationMapper::convertToDto)
                .toList();
        return ResponseEntity.ok(mutilations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MutilationDto> getMutilationById(@PathVariable Long id) {
        MutilationDto mutilationDto = mutilationMapper.convertToDto(mutilationService.findMutilationById(id));
        return ResponseEntity.ok(mutilationDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MutilationDto> updateMutilation(@PathVariable Long id, @RequestBody MutilationRequest mutilationDto) {
        return ResponseEntity.ok(mutilationMapper.convertToDto(mutilationService.updateMutilation(id, mutilationDto)));
    }

    @DeleteMapping("/change/{id}")
    public ResponseEntity<Void> deleteMutilation(@PathVariable Long id) {
        mutilationService.deleteMutilation(id);
        return ResponseEntity.noContent().build();
    }
}
