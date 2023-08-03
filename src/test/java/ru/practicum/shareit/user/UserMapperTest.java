package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Test
    @DirtiesContext
    public void createUserDtoTest() {
        User user = new User(1L, "name", "name@user.ru");
        UserDto userDto = UserMapper.createUserDto(user);
        assertEquals(1L, userDto.getId());
        assertEquals("name", userDto.getName());
        assertEquals("name@user.ru", userDto.getEmail());
    }

    @Test
    @DirtiesContext
    public void createUserTest() {
        UserDto userDto = new UserDto("nameDto", "namedto@userdto.ru");
        userDto.setId(2L);
        User user = UserMapper.createUser(userDto);
        assertEquals(2L, user.getId());
        assertEquals("nameDto", user.getName());
        assertEquals("namedto@userdto.ru", user.getEmail());
    }
}
