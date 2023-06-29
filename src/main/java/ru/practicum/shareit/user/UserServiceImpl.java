package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public UserDto createUser(UserDto userDto) {
        for (User thisUser : userStorage.findAll()) {
            if (thisUser.getEmail().equals(userDto.getEmail())) {
                throw new UserAlreadyExistsException("Пользователь с таким Email уже зарегистрирован");
            }
        }
        User user = UserMapper.createUser(userDto);
        userValidator(user);
        userStorage.create(user);
        return UserMapper.createUserDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::createUserDto)
                .collect(toList());
    }

    @Override
    public UserDto getUser(Integer userId) {
        User user = userFromStorage(userId);
        return UserMapper.createUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userFromStorage(userDto.getId());
        User userToUpdate = UserMapper.createUser(userDto);
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
        return UserMapper.createUserDto(updatedUser);
    }

    @Override
    public void delete(Integer userId) {
        User user = userFromStorage(userId);
        for (Item item : itemStorage.getItemsByOwner(user.getId())) {
            itemStorage.deleteItem(item.getId());
        }
        userStorage.delete(user);
    }

    private void userValidator(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if (user.getName() == null || user.getName().isBlank()) {
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

    @Override
    public User userFromStorage(Integer userId) {
        return userStorage.getUser(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + userId + " не существует."));
    }
}