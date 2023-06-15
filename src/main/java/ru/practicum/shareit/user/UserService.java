package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
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

    public User createUser(UserDto userDto) {
        return userStorage.create(userMapper.createUser(userDto));
    }

    public List<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(userMapper::createUserDto)
                .collect(toList());
    }

    public UserDto getUser(int userId) {
        return userMapper.createUserDto(userStorage.getUser(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + userId + " не существует.")));
    }

    public UserDto update(UserDto userDto) {
        User user = userMapper.createUser(userDto);
        if (validateUser(user)) {
            userStorage.update(user);
        }
        return userMapper.createUserDto(userStorage.getUser(user.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + user.getId() + " не существует.")));
    }

    public void delete(UserDto userDto) {
        User user = userMapper.createUser(userDto);
        if (validateUser(user)) {
            for (Item item : itemStorage.findAll()) {
                if (item.getOwner().equals(user)) {
                    itemStorage.deleteItem(item.getId(), user.getId());
                }
            }
            userStorage.delete(user);
        }
    }

    private boolean validateUser(User user) {
        return userStorage.findAll().contains(user);
    }
}