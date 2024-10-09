package org.example.sports.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sports.controller.user.dto.CreateUserRequest;
import org.example.sports.controller.user.dto.UpdateUserRequest;
import org.example.sports.model.AuthorizationData;
import org.example.sports.model.User;
import org.example.sports.model.enums.Role;
import org.example.sports.repositore.AuthorizationRepository;
import org.example.sports.repositore.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorizationRepository authorizationRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(CreateUserRequest userDto) {
        User user = User.builder()
                .username(userDto.username())
                .nick(userDto.nick())
                .telegram(userDto.telegram())
                .role(Role.valueOf(userDto.role()))
                .build();

        AuthorizationData auth = AuthorizationData.builder()
                .username(userDto.username())
                .password(passwordEncoder.encode(userDto.password()))
                .user(user)
                .build();

        user.setAuthorizationData(auth);
        if (authorizationRepository.findById(auth.getUsername()).isPresent()) {
            throw new IllegalArgumentException("This username " + auth.getUsername() + " is already in use.");
        }

        User createdUser = userRepository.save(user);
        authorizationRepository.save(auth);

        return createdUser;
    }

    public User getUserById(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + username + "' not found"));
    }


    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new EntityNotFoundException("User with username '" + username + "' not found");
        }
        userRepository.deleteById(username);
    }

    @Transactional
    public User updateUser(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + username + "' not found"));


        user.setNick(updateUserRequest.nick());
        user.setTelegram(updateUserRequest.telegram());
        return userRepository.save(user);
    }


}
