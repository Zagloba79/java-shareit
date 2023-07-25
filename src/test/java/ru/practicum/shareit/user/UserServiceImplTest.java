package ru.practicum.shareit.user;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class UserServiceImplTest {

    public UserDto create(UserDto userDto) {
        User user = UserMapper.createUser(userDto);
        userValidate(user);
        User savedUser = userRepository.save(user);
        return UserMapper.createUserDto(savedUser);
    }

    public void userCreateTest() {

    }
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::createUserDto)
                .collect(toList());
    }


    public UserDto getUserDto(Long userId) {
        User user = handler.getUserFromOpt(userId);
        return UserMapper.createUserDto(user);
    }


    public User getUser(Long userId) {
        return handler.getUserFromOpt(userId);
    }


    public UserDto update(UserDto userDto, Long id) {
        User user = getUser(userDto.getId());
        User userFromDto = UserMapper.createUser(userDto);
        if (userFromDto.getName() != null) {
            user.setName(userFromDto.getName());
        }
        if (emailValidate(userFromDto.getEmail(), userFromDto.getId())) {
            user.setEmail(userFromDto.getEmail());
        }
        userRepository.save(user);
        return UserMapper.createUserDto(getUser(user.getId()));
    }


    public void delete(Long userId) {
        User user = handler.getUserFromOpt(userId);
        List<Item> itemsByOwner = itemRepository.findByOwnerId(userId, null);
        for (Item item : itemsByOwner) {
            itemRepository.deleteById(item.getId());
        }
        userRepository.delete(user);
    }

}
