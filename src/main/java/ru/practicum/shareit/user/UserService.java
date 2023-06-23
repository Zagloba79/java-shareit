package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private UserStorage userStorage;
    private UserMapper userMapper;
    private ItemStorage itemStorage;
    private static Integer id = 0;

    public UserDto createUser(UserDto userDto) {
        for (User thisUser : userStorage.findAll()) {
            if (thisUser.getEmail().equals(userDto.getEmail())) {
                throw new UserAlreadyExistsException("Пользователь с таким Email уже зарегистрирован");
            }
        }
        User user = userMapper.createUser(userDto);
        user.setId(id++);
        userStorage.create(user);
        return userMapper.createUserDto(user);
    }

    public List<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(userMapper::createUserDto)
                .collect(toList());
    }

    public UserDto getUser(Integer userId) {
        User user = userStorage.getUser(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + userId + " не существует."));
        return userMapper.createUserDto(user);
    }

    public UserDto update(UserDto userDto) {
        User user = userMapper.createUser(getUser(userDto.getId()));
        userStorage.update(user);
        User updatedUser = userStorage.getUser(user.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + user.getId() + " не существует."));
        return userMapper.createUserDto(updatedUser);
    }

    public void delete(UserDto userDto) {
        User user = userMapper.createUser(getUser(userDto.getId()));
        for (Item item : itemStorage.findAll()) {
            if (item.getOwner().equals(user)) {
                itemStorage.deleteItem(item.getId());
            }
        }
        userStorage.delete(user);
    }
}