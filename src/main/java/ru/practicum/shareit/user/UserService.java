package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> findAll();

    UserDto getUser(Long userId);

    UserDto update(UserDto userDto);

    void delete(Long userId);
}