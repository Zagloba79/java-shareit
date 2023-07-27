package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceTest {
    private final UserService service;

    @BeforeEach
    void clearDb() {
        List<UserDto> users = service.findAll();
        for (UserDto user : users) {
            service.delete(user.getId());
        }
    }

    @Test
    void shouldReturnUserWhenGetUser() {
        UserDto userDto = new UserDto("User", "user@users.ru");
        UserDto userFromDb = service.create(userDto);
        assertEquals(userFromDb.getName(), userDto.getName());
        assertEquals(userFromDb.getEmail(), userDto.getEmail());
    }

    @Test
    void exceptionWhenEmailIsTakenYet() {
        UserDto userDto = new UserDto("user", "user@users.ru");
        service.create(userDto);
        UserDto abuserDto = new UserDto("abuser", "abuser@users.ru");
        UserDto userFromDb = service.create(abuserDto);
        abuserDto.setId(userFromDb.getId());
        abuserDto.setEmail("user@users.ru");
        final UserAlreadyExistsException exception = Assertions.assertThrows(
                UserAlreadyExistsException.class,
                () -> service.update(abuserDto, userFromDb.getId()));
        assertEquals("Пользователь с таким Email уже зарегистрирован", exception.getMessage());
    }

    @Test
    void shouldReturnUsersWhenFindAll() {
        UserDto user1 = new UserDto("User1", "user1@users.ru");
        UserDto user2 = new UserDto("User2", "user2@users.ru");
        UserDto user3 = new UserDto("User3", "user3@users.ru");
        service.create(user1);
        service.create(user2);
        service.create(user3);
        List<UserDto> users = service.findAll();
        assertEquals(3, users.size());
    }

    @Test
    void shouldReturnExceptionWhenDeleteUserWithWrongId() {
        UserDto user1 = new UserDto("User1", "user1@users.ru");
        UserDto user2 = new UserDto("User2", "user2@users.ru");
        UserDto user3 = new UserDto("User3", "user3@users.ru");
        service.create(user1);
        service.create(user2);
        service.create(user3);
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> service.delete(135L));
        assertEquals("Пользователя с " + 135L + " не существует.", exception.getMessage());
    }

    @Test
    void shouldDeleteUserByUserId() {
        UserDto user1 = new UserDto("User1", "user1@users.ru");
        UserDto user2 = new UserDto("User2", "user2@users.ru");
        UserDto user3 = new UserDto("User3", "user3@users.ru");
        UserDto u1 = service.create(user1);
        UserDto u2 = service.create(user2);
        UserDto u3 = service.create(user3);
        assertEquals(3, service.findAll().size());
        service.delete(u2.getId());
        assertEquals(2, service.findAll().size());
        assertEquals("User1", service.getUser(u1.getId()).getName());
        assertEquals("User3", service.getUser(u3.getId()).getName());
    }
}