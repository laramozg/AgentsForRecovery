package org.example.sports.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.example.sports.controller.user.dto.UserDto;
import org.example.sports.mapper.UserMapper;
import org.example.sports.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(userMapper.convertToDto(userService.createUser(request)), HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userMapper.convertToDto(userService.getUserById(username)));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userMapper.convertToDto(userService.updateUser(username, request)));
    }
}
