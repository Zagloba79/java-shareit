package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    private ItemRequestClient itemRequestClient;
    @InjectMocks
    private ItemRequestController itemRequestController;
    UserDto userDto = new UserDto(1L, "hgf", "kij@er.ty");
    private final ItemRequestDto requestDto = new ItemRequestDto(5L, "descxz", userDto,
            LocalDateTime.of(2023, 1, 2, 3, 4, 5), null);

    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @Test
    public void createRequestTest() {
        when(itemRequestClient.create(requestDto, userDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> objectResponseEntity1 = itemRequestController.create(requestDto, userDto.getId());
        assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    public void getOwnItemRequestsTest() {
        when(itemRequestClient.getOwnItemRequests(userDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> listRequestUser = itemRequestController.getOwnItemRequests(userDto.getId());
        assertEquals(listRequestUser, objectResponseEntity);
    }

    @Test
    public void getAllItemRequestsTest() {
        int from = 1;
        int size = 10;
        when(itemRequestClient.getAllItemRequests(userDto.getId(), from, size)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> listRequest = itemRequestController.getAllItemRequests(userDto.getId(), from, size);
        assertEquals(listRequest, objectResponseEntity);
    }

    @Test
    public void getItemRequestByIdTest() {
        when(itemRequestClient.getItemRequestById(userDto.getId(), requestDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> itemRequest = itemRequestController.getItemRequestById(userDto.getId(), requestDto.getId());
        assertEquals(objectResponseEntity, itemRequest);
    }
}