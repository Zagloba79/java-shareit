package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    List<UserDto> findAll();

    UserDto getUserDto(Long userId);

    User getUser(Long userId);

    UserDto update(UserDto userDto, Long id);

    void delete(Long userId);
}