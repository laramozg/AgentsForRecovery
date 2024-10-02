package org.example.sports.service;

import lombok.RequiredArgsConstructor;
import org.example.sports.exceptions.BadRequestException;
import org.example.sports.exceptions.UserNotFoundException;
import org.example.sports.model.dto.CreateUserRequest;
import org.example.sports.model.dto.UserDto;
import org.example.sports.model.entity.AuthorizationData;
import org.example.sports.model.entity.User;
import org.example.sports.model.entity.enums.Role;
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

    public UserDto createUser(UserDto userDto, String password, String role) {
        if (userDto.username() == null || userDto.nick() == null || userDto.telegram() == null) {
            throw new BadRequestException("All fields must be filled.");
        }

        User user = User.builder()
                .username(userDto.username())
                .nick(userDto.nick())
                .telegram(userDto.telegram())
                .build();

        AuthorizationData auth = AuthorizationData.builder()
                .username(userDto.username())
                .password(passwordEncoder.encode(password))
                .role(Role.valueOf(role))
                .user(user)
                .build();

        user.setAuthorizationData(auth);
        if (authorizationRepository.findById(auth.getUsername()).isPresent()) {
            throw new IllegalArgumentException("This username" + auth.getUsername() + "is already in use.");
        }

        userRepository.save(user);
        authorizationRepository.save(auth);

        return userDto;
    }

    public UserDto getUser(String username) {
        return userRepository.findById(username)
                .map(user -> new UserDto(user.getUsername(), user.getNick(), user.getTelegram()))
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
    }


    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new UserNotFoundException("User with username '" + username + "' not found");
        }
        userRepository.deleteById(username);
    }

    public UserDto updateUser(String username, UserDto userDto) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        if (userDto.nick() == null || userDto.telegram() == null) {
            throw new BadRequestException("Nick and Telegram must not be null.");
        }

        user.setNick(userDto.nick());
        user.setTelegram(userDto.telegram());
        userRepository.save(user);
        return userDto;
    }


}
