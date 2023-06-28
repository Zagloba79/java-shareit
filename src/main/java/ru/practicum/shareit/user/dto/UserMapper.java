package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public final class UserMapper {
    private UserMapper() {
    }

    public static UserDto createUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User createUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}