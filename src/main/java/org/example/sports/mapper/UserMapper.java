package org.example.sports.mapper;


import org.example.sports.controller.user.dto.UserDto;
import org.example.sports.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
                user.getUsername(),
                user.getNick(),
                user.getTelegram());
    }
}
