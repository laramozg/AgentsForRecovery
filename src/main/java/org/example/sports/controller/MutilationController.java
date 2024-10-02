package org.example.sports.controller;

import lombok.RequiredArgsConstructor;
import org.example.sports.service.MutilationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sports")
@RequiredArgsConstructor
public class MutilationController {
    private final MutilationService mutilationService;

    @GetMapping("/mutilation/all")
    public ResponseEntity<?> findAllMutilations() {
        return ResponseEntity.ok(mutilationService.findAllMutilation());
    }
}
