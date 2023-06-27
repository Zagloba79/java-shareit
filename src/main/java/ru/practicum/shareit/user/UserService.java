package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;
    private final ItemStorage itemStorage;
    private Integer userId = 1;

    @Autowired
    public UserService(UserStorage userStorage, UserMapper userMapper, ItemStorage itemStorage) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
        this.itemStorage = itemStorage;
    }

    public UserDto createUser(UserDto userDto) {
        for (User thisUser : userStorage.findAll()) {
            if (thisUser.getEmail().equals(userDto.getEmail())) {
                throw new UserAlreadyExistsException("Пользователь с таким Email уже зарегистрирован");
            }
        }
        User user = userMapper.createUser(userDto);
        userValidator(user);
        user.setId(userId++);
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
        User user = userStorage.getUser(userDto.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + userDto.getId() + " не существует."));
        User userToUpdate = userMapper.createUser(userDto);
        if (userToUpdate.getName() != null) {
            user.setName(userToUpdate.getName());
        }
        if (emailValidate(userToUpdate.getEmail(), userToUpdate.getId())) {
            user.setEmail(userToUpdate.getEmail());
        }
        userStorage.update(user);
        User updatedUser = new User();
        Optional<User> updatedUserOpt = userStorage.getUser(user.getId());
        if (updatedUserOpt.isPresent()) {
            updatedUser = updatedUserOpt.get();
        }
        return userMapper.createUserDto(updatedUser);
    }

    public void delete(Integer userId) {
        User user = userStorage.getUser(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + userId + " не существует."));
        for (Item item : itemStorage.getItemsByOwner(user.getId())) {
            itemStorage.deleteItem(item.getId());
        }
        userStorage.delete(user);
    }

    private void userValidator(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            throw new ValidationException("Некорректный логин пользователя: " + user.getName());
        }
    }

    private boolean emailValidate(String email, Integer id) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            return false;
        }
        for (User user : userStorage.findAll()) {
            if (!user.getId().equals(id) && user.getEmail().equals(email)) {
                throw new UserAlreadyExistsException("Пользователь с таким Email уже зарегистрирован");
            }
        }
        return true;
    }
}