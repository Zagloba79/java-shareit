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
    private UserClient userClient;
    @InjectMocks
    private UserController userController;
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    private final UserDto userDto = new UserDto(1L, "hgf", "kij@er.ty");

    @Test
    public void createUserTest() {
        when(userClient.create(userDto)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> user = userController.create(userDto);
        assertEquals(user, objectResponseEntity);
    }

    @Test
    public void updateUserTest() {
        when(userClient.update(userDto, userDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> objectResponseEntity1 = userController.update(userDto, userDto.getId());
        assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    public void getAllUsersTest() {
        when(userClient.getUsers()).thenReturn(objectResponseEntity);
        ResponseEntity<Object> allUsers = userController.getUsers();
        assertEquals(objectResponseEntity, allUsers);
    }

    @Test
    public void getUserTest() {
        when(userClient.getUserById(userDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> user = userController.getUserById(userDto.getId());
        assertEquals(objectResponseEntity, user);
    }

    @Test
    public void deleteUserTest() {
        userController.delete(userDto.getId());
        Mockito.verify(userClient, Mockito.times(1)).delete(userDto.getId());
    }
}