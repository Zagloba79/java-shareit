package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handler.EntityHandler;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final EntityHandler entityHandler;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.createUser(userDto);
        userValidator(user);
        User savedUser = userRepository.save(user);
        return UserMapper.createUserDto(savedUser);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::createUserDto)
                .collect(toList());
    }

    @Override
    public UserDto getUserDto(Long userId) {
        User user = entityHandler.getUserFromOpt(userId);
        return UserMapper.createUserDto(user);
    }

    @Override
    public User getUser(Long userId) {
        return entityHandler.getUserFromOpt(userId);
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        User user = getUser(userDto.getId());
        User userToUpdate = UserMapper.createUser(userDto);
        if (userToUpdate.getName() != null) {
            user.setName(userToUpdate.getName());
        }
        if (emailValidate(userToUpdate.getEmail(), userToUpdate.getId())) {
            user.setEmail(userToUpdate.getEmail());
        }
        userRepository.save(user);
        return UserMapper.createUserDto(getUser(user.getId()));
    }

    @Override
    public void delete(Long userId) {
        User user = entityHandler.getUserFromOpt(userId);
        List<Item> itemsByOwner = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(toList());
        for (Item item : itemsByOwner) {
            itemRepository.deleteById(item.getId());
        }
        userRepository.delete(user);
    }

    private void userValidator(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Некорректный логин пользователя: " + user.getName());
        }
    }

    private boolean emailValidate(String email, Long id) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            return false;
        }
        for (User user : userRepository.findAll()) {
            if (!user.getId().equals(id) && user.getEmail().equals(email)) {
                throw new UserAlreadyExistsException("Пользователь с таким Email уже зарегистрирован");
            }
        }
        return true;
    }
}