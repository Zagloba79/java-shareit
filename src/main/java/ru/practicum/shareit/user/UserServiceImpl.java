package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
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

    private final EntityHandler handler;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.createUser(userDto);
        userValidate(user);
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
        User user = handler.getUserFromOpt(userId);
        return UserMapper.createUserDto(user);
    }

    @Override
    public User getUser(Long userId) {
        return handler.getUserFromOpt(userId);
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        User userFromDb = getUser(userDto.getId());
        User userFromDto = UserMapper.createUser(userDto);
        if (userFromDto.getName() != null) {
            userFromDb.setName(userFromDto.getName());
        }
        if (emailValidate(userFromDto.getEmail(), userFromDto.getId())) {
            userFromDb.setEmail(userFromDto.getEmail());
        }
        userRepository.save(userFromDb);
        return UserMapper.createUserDto(getUser(userFromDb.getId()));
    }

    @Override
    public void delete(Long userId) {
        User user = handler.getUserFromOpt(userId);
        List<Item> itemsByOwner = itemRepository.findByOwnerId(userId, null);
        for (Item item : itemsByOwner) {
            itemRepository.deleteById(item.getId());
        }
        userRepository.delete(user);
    }

    private void userValidate(User user) {
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