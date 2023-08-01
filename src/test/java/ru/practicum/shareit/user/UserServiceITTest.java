package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceITTest {
    private final UserService service;
    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @Test
    @DirtiesContext
    public void shouldReturnUserWhenGetUserTest() {
        UserDto userDto = new UserDto("User", "user@users.ru");
        UserDto userFromDb = service.create(userDto);
        assertEquals(userFromDb.getName(), userDto.getName());
        assertEquals(userFromDb.getEmail(), userDto.getEmail());
    }

    @Test
    @DirtiesContext
    void shouldGetUserDtoTest() {
        UserDto userDto = new UserDto("User", "user@users.ru");
        userDto = service.create(userDto);
        UserDto userFromDb = service.getUserDto(userDto.getId());
        assertEquals("User", userFromDb.getName());
        assertEquals("user@users.ru", userFromDb.getEmail());
    }


    @Test
    @DirtiesContext
    public void exceptionWhenEmailIsTakenYetTest() {
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
    @DirtiesContext
    public void exceptionWhenUserUpdateWithWrongEmailTest() {
        UserDto userDto = new UserDto("user", "user@users.ru");
        userDto = service.create(userDto);
        Long id = userDto.getId();
        userDto.setEmail(null);
        userDto = service.update(userDto, userDto.getId());
        assertEquals(id, userDto.getId());
        assertEquals("user", userDto.getName());
        assertEquals("user@users.ru", userDto.getEmail());
        userDto.setEmail("users.ru");
        userDto = service.update(userDto, userDto.getId());
        assertEquals(id, userDto.getId());
        assertEquals("user", userDto.getName());
        assertEquals("user@users.ru", userDto.getEmail());
        userDto.setEmail("users@ru");
        userDto = service.update(userDto, userDto.getId());
        assertEquals(id, userDto.getId());
        assertEquals("user", userDto.getName());
        assertEquals("user@users.ru", userDto.getEmail());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenEmailIsWrongTest() {
        UserDto userDto = new UserDto("user", null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> service.create(userDto));
        assertEquals("Некорректный e-mail пользователя", exception.getMessage());
        userDto.setEmail("faew.fa");
        exception = Assertions.assertThrows(
                ValidationException.class,
                () -> service.create(userDto));
        assertEquals("Некорректный e-mail пользователя", exception.getMessage());
        userDto.setEmail("faew@fa");
        exception = Assertions.assertThrows(
                ValidationException.class,
                () -> service.create(userDto));
        assertEquals("Некорректный e-mail пользователя", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void shouldUpdateUsersTest() {
        UserDto userDto = new UserDto("user", "user@users.ru");
        userDto = service.create(userDto);
        Long id = userDto.getId();
        assertEquals(id, userDto.getId());
        assertEquals("user", userDto.getName());
        assertEquals("user@users.ru", userDto.getEmail());
        userDto.setName("abuser");
        userDto = service.update(userDto, id);
        assertEquals(id, userDto.getId());
        assertEquals("abuser", userDto.getName());
        assertEquals("user@users.ru", userDto.getEmail());
        userDto.setEmail("abuser@users.ru");
        assertEquals(id, userDto.getId());
        assertEquals("abuser", userDto.getName());
        assertEquals("abuser@users.ru", userDto.getEmail());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenNameIsWrongTest() {
        UserDto userDto = new UserDto(null, "null@user.ru");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> service.create(userDto));
        assertEquals("Некорректный логин пользователя", exception.getMessage());
        userDto.setName("        ");
        exception = Assertions.assertThrows(
                ValidationException.class,
                () -> service.create(userDto));
        assertEquals("Некорректный логин пользователя", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void shouldReturnUsersWhenFindAllTest() {
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
    @DirtiesContext
    public void shouldReturnExceptionWhenDeleteUserWithWrongIdTest() {
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
    @DirtiesContext
    public void shouldDeleteUserByUserIdTest() {
        UserDto user1 = new UserDto("User1", "user1@users.ru");
        UserDto user2 = new UserDto("User2", "user2@users.ru");
        UserDto user3 = new UserDto("User3", "user3@users.ru");
        UserDto u1 = service.create(user1);
        UserDto u2 = service.create(user2);
        UserDto u3 = service.create(user3);
        ItemDto item1 = new ItemDto("item1", "d1", true, null);
        ItemDto item2 = new ItemDto("item2", "d2", true, null);
        itemService.create(item1, u2.getId());
        itemService.create(item2, u2.getId());
        assertEquals(3, service.findAll().size());
        assertEquals(2, itemRepository.findAll().size());
        service.delete(u2.getId());
        assertEquals(2, service.findAll().size());
        assertEquals("User1", service.getUser(u1.getId()).getName());
        assertEquals("User3", service.getUser(u3.getId()).getName());
        assertEquals(0, itemRepository.findAll().size());
    }
}