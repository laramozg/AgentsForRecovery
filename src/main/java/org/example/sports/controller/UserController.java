package org.example.sports.controller;

import lombok.RequiredArgsConstructor;
import org.example.sports.model.dto.CreateUserRequest;
import org.example.sports.model.dto.UserDto;
import org.example.sports.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request) {
        UserDto createdUser = userService.createUser(
                new UserDto(request.username(), request.nick(), request.telegram()),
                request.password(),
                request.role()
        );
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String username, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(username, userDto));
    }
}
