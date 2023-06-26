package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {
    public UserDto createUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public User createUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}