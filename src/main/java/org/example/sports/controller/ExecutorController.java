package org.example.sports.controller;

import lombok.RequiredArgsConstructor;
import org.example.sports.model.dto.ExecutorDto;
import org.example.sports.model.dto.UserDto;
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
    public ResponseEntity<ExecutorDto> createExecutor(@RequestBody ExecutorDto request) {

        return new ResponseEntity<>(executorService.createExecutor(request), HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ExecutorDto> getUser(@PathVariable String username) {
        return ResponseEntity.ok(executorService.getUser(username));
    }


    @PutMapping("/{username}")
    public ResponseEntity<ExecutorDto> updateUser(@PathVariable String username, @RequestBody ExecutorDto executorDto) {
        return ResponseEntity.ok(executorService.updateUser(username, executorDto));
    }
}
