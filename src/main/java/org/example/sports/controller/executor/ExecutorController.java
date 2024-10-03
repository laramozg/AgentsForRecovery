package org.example.sports.controller.executor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.executor.dto.ExecutorDto;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.service.ExecutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports/user/executor")
@RequiredArgsConstructor
public class ExecutorController {

    private final ExecutorService executorService;

    @PostMapping
    public ResponseEntity<ExecutorDto> createExecutor(@Valid @RequestBody ExecutorRequest request) {
        return new ResponseEntity<>(executorService.createExecutor(request), HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ExecutorDto> getExecutor(@PathVariable String username) {
        return ResponseEntity.ok(executorService.getUser(username));
    }


    @PutMapping("/{username}")
    public ResponseEntity<ExecutorDto> updateExecutor(
            @PathVariable String username, @Valid @RequestBody ExecutorRequest request) {
        return ResponseEntity.ok(executorService.updateUser(username, request));
    }
}
