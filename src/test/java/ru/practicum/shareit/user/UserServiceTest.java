package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceTest {
    private final UserService service;
    private final UserDto userDto = new UserDto("mnb", "mnb@uyt.ru");

    @Test
    public void createAndUpdateUserTest() {
        UserDto userFromDb = service.create(userDto);
        assertEquals("mnb", userFromDb.getName());
        assertEquals("mnb@uyt.ru", userFromDb.getEmail());
        UserDto newUser = new UserDto("asd", null);
        newUser.setId(userFromDb.getId());
        UserDto updatedUser = service.update(newUser, newUser.getId());
        assertEquals("asd", updatedUser.getName());
        assertEquals("mnb@uyt.ru", updatedUser.getEmail());
        newUser = new UserDto(null, "asd@uyt.ru");
        newUser.setId(userFromDb.getId());
        updatedUser = service.update(newUser, newUser.getId());
        assertEquals("asd", updatedUser.getName());
        assertEquals("asd@uyt.ru", updatedUser.getEmail());
    }

    @Test
    public void deleteUserTest() {
        UserDto userFromDb = service.create(userDto);
        Long userId = userFromDb.getId();
        assertEquals("mnb", userFromDb.getName());
        assertEquals("mnb@uyt.ru", userFromDb.getEmail());
        service.delete(userId);
        final ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> service.getUser(userId));
        assertEquals("Пользователя с " + userId + " не существует.",
                exception.getMessage());
    }
}
