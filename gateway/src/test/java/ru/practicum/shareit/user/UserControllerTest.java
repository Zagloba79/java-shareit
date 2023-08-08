package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserClient userClient;
    @InjectMocks
    private UserController userController;
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    private final UserDto userDto = new UserDto(1L, "hgf", "kij@er.ty");

    @Test
    public void createUserTest() {
        Mockito.when(userClient.create(userDto)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> user = userController.create(userDto);
        Assertions.assertEquals(user, objectResponseEntity);
    }

    @Test
    public void updateUserTest() {
        Mockito.when(userClient.update(userDto, userDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> objectResponseEntity1 = userController.update(userDto, userDto.getId());
        Assertions.assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    public void findAllUsers() {
        Mockito.when(userClient.getUsers()).thenReturn(objectResponseEntity);
        ResponseEntity<Object> allUsers = userController.getUsers();
        Assertions.assertEquals(objectResponseEntity, allUsers);
    }

    @Test
    public void findUser() {
        Mockito.when(userClient.getUserById(userDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> user = userController.getUserById(userDto.getId());
        Assertions.assertEquals(objectResponseEntity, user);
    }

    @Test
    public void deleteUser() {
        userController.delete(userDto.getId());
        Mockito.verify(userClient, Mockito.times(1)).delete(userDto.getId());
    }
}