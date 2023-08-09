package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerITTest {
    @Mock
    private UserClient client;
    @InjectMocks
    private UserController controller;
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    private final UserDto userDto = new UserDto(1L, "hgf", "kij@er.ty");

    @Test
    public void createUserTest() {
        when(client.create(userDto)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.create(userDto);
        assertEquals(response, objectResponseEntity);
    }

    @Test
    public void updateUserTest() {
        when(client.update(userDto, userDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.update(userDto, userDto.getId());
        assertEquals(objectResponseEntity, response);
    }

    @Test
    public void getAllUsersTest() {
        when(client.getUsers()).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.getUsers();
        assertEquals(objectResponseEntity, response);
    }

    @Test
    public void getUserTest() {
        when(client.getUserById(userDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.getUserById(userDto.getId());
        assertEquals(objectResponseEntity, response);
    }

    @Test
    public void deleteUserTest() {
        controller.delete(userDto.getId());
        Mockito.verify(client, Mockito.times(1)).delete(userDto.getId());
    }
}