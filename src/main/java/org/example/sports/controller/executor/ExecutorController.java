package org.example.sports.controller.executor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.executor.dto.ExecutorDto;
import org.example.sports.controller.executor.dto.ExecutorRequest;
import org.example.sports.mapper.ExecutorMapper;
import org.example.sports.model.Executor;
import org.example.sports.service.ExecutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports/user/executor")
@RequiredArgsConstructor
public class ExecutorController {

    private final ExecutorService executorService;
    private final ExecutorMapper executorMapper;

    @PostMapping
    public ResponseEntity<ExecutorDto> createExecutor(@Valid @RequestBody ExecutorRequest request) {
        Executor executor = executorService.createExecutor(executorMapper.convert(request));
        return new ResponseEntity<>(executorMapper.convertToDto(executor), HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ExecutorDto> getExecutor(@PathVariable String username) {
        return ResponseEntity.ok(executorMapper.convertToDto(executorService.getExecutorById(username)));
    }


    @PutMapping
    public ResponseEntity<ExecutorDto> updateExecutor(
            @Valid @RequestBody ExecutorRequest request) {
        Executor executor = executorService.updateExecutor(request);
        return ResponseEntity.ok(executorMapper.convertToDto(executor));
    }
}
