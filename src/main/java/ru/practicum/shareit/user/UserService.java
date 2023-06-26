package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class UserService {
    private UserStorage userStorage;
    private UserMapper userMapper;
    private ItemStorage itemStorage;
    private static Integer id = 1;

    public UserDto createUser(UserDto userDto) {
        for (User thisUser : userStorage.findAll()) {
            if (thisUser.getEmail().equals(userDto.getEmail())) {
                throw new UserAlreadyExistsException("Пользователь с таким Email уже зарегистрирован");
            }
        }
        User user = userMapper.createUser(userDto);
        if (userValidator(user)) {
            user.setId(id++);
            userStorage.create(user);
        }
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
        User user = userStorage.getUser(userDto.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + userDto.getId() + " не существует."));
        userValidator(user);
        User userToUpdate = userMapper.createUser(userDto);
        userStorage.update(userToUpdate);
        return userMapper.createUserDto(userToUpdate);
    }

    public void delete(Integer id) {
        User user = userMapper.createUser(getUser(id));
        for (Item item : itemStorage.findAll()) {
            if (item.getOwner().equals(user)) {
                itemStorage.deleteItem(item.getId());
            }
        }
        userStorage.delete(user);
    }

    private boolean userValidator(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            throw new ValidationException("Некорректный логин пользователя: " + user.getName());
        }
        return true;
    }
}