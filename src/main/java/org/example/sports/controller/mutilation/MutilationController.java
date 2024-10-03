package org.example.sports.controller.mutilation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.mutilation.dto.CreateMutilation;
import org.example.sports.controller.mutilation.dto.MutilationDto;
import org.example.sports.service.MutilationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports/user/mutilation")
@RequiredArgsConstructor
public class MutilationController {
    private final MutilationService mutilationService;

    @PostMapping("/change")
    public ResponseEntity<MutilationDto> createMutilation(@Valid @RequestBody CreateMutilation createMutilation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mutilationService.createMutilation(createMutilation));
    }

    @GetMapping
    public ResponseEntity<Page<MutilationDto>> getAllMutilations(Pageable pageable) {
        Page<MutilationDto> mutilations = mutilationService.findAllMutilations(pageable);
        return ResponseEntity.ok(mutilations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MutilationDto> getMutilationById(@PathVariable Long id) {
        MutilationDto mutilationDto = mutilationService.findMutilationById(id);
        return ResponseEntity.ok(mutilationDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MutilationDto> updateMutilation(@PathVariable Long id, @RequestBody CreateMutilation mutilationDto) {
        return ResponseEntity.ok(mutilationService.updateMutilation(id, mutilationDto));
    }

    @DeleteMapping("/change/{id}")
    public ResponseEntity<Void> deleteMutilation(@PathVariable Long id) {
        mutilationService.deleteMutilation(id);
        return ResponseEntity.noContent().build();
    }
}
