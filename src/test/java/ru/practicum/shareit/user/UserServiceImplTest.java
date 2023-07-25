package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private UserRepository userRepository;
    @Mock
    EntityHandler handler;
    private final UserDto userDto = new UserDto("mnb", "mnb@uyt.ru");

    @Test
    public void userCreateTest() {
        User user = UserMapper.createUser(userDto);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        UserDto userFromDb = service.create(userDto);
        assertEquals("mnb", userFromDb.getName());
    }

//    @Test
//    void createUserWithExistingEmail() {
//        User user = new User("mnb", "mnb@uyt.ru");
//        User duble = new User("gre", "mnb@uyt.ru");
//        Mockito.when(userRepository.save(user)).thenReturn(user);
//        Mockito.when(userRepository.save(duble)).thenThrow(UserAlreadyExistsException.class);
//        assertThrows(UserAlreadyExistsException.class, () -> service.create(duble));
//    }


//    public List<UserDto> findAll() {
//        return userRepository.findAll().stream()
//                .map(UserMapper::createUserDto)
//                .collect(toList());
//    }


//
//    public UserDto getUserDto(Long userId) {
//        User user = handler.getUserFromOpt(userId);
//        return UserMapper.createUserDto(user);
//    }
//
//
//    public User getUser(Long userId) {
//        return handler.getUserFromOpt(userId);
//    }
//
//
//    public UserDto update(UserDto userDto, Long id) {
//        User user = getUser(userDto.getId());
//        User userFromDto = UserMapper.createUser(userDto);
//        if (userFromDto.getName() != null) {
//            user.setName(userFromDto.getName());
//        }
//        if (emailValidate(userFromDto.getEmail(), userFromDto.getId())) {
//            user.setEmail(userFromDto.getEmail());
//        }
//        userRepository.save(user);
//        return UserMapper.createUserDto(getUser(user.getId()));
//    }
//
//
//    public void delete(Long userId) {
//        User user = handler.getUserFromOpt(userId);
//        List<Item> itemsByOwner = itemRepository.findByOwnerId(userId, null);
//        for (Item item : itemsByOwner) {
//            itemRepository.deleteById(item.getId());
//        }
//        userRepository.delete(user);
//    }

}
