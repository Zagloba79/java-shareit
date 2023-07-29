package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemRequestServiceITTest {
    private final ItemRequestService service;
    private final ItemRequestRepository repository;
    private final UserService userService;
    private User user;
    private final LocalDateTime created = LocalDateTime.of(
            2023, 1,2,3,4,5);

    @BeforeEach
    public void setUp() {
        UserDto userDto = new UserDto("name", "q@ma.ru");
        user = UserMapper.createUser(userService.create(userDto));
    }

    @AfterEach
    public void clearDb() {
        repository.deleteAll();
        List<UserDto> users = userService.findAll();
        for (UserDto user : users) {
            userService.delete(user.getId());
        }
    }

    @Test
    void exceptionWhenCreateItemRequestWithWrongUserId() {
        Long id = -35672L;
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> service.create(new ItemRequestDto(), id));
        assertEquals("Пользователя с " + id + " не существует.", exception.getMessage());
    }

    @Test
    public void shouldReturnRequestWhenGetRequestById() {
        ItemRequestDto requestDto = new ItemRequestDto("description", created);
        ItemRequestDto requestFromDb = service.create(requestDto, user.getId());
        assertEquals(requestFromDb.getDescription(), requestDto.getDescription());
    }

    @Test
    public void exceptionWhenGetRequestByWrongId() {
        final ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> service.getRequestById(1L, user.getId()));
        assertEquals("Запроса с " + 1L + " не существует.",
                exception.getMessage());
    }

    @Test
    public void exceptionWhenGetRequestByWrongRequesterId() {
        ItemRequestDto requestDto = new ItemRequestDto("description", LocalDateTime.now());
        ItemRequestDto requestDtoFromDb = service.create(requestDto, user.getId());
        final ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> service.getRequestById(requestDtoFromDb.getId(), 5L));
        assertEquals("Пользователя с " + 5L + " не существует.",
                exception.getMessage());
    }

    @Test
    public void exceptionWhenGetRequestByWrongRequester() {
        final ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> service.getRequestsByRequester(99L));
        assertEquals("Пользователя с " + 99L + " не существует.",
                exception.getMessage());
    }
}