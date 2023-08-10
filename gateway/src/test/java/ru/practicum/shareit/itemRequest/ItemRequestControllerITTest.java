package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerITTest {
    @Mock
    private ItemRequestClient client;
    @InjectMocks
    private ItemRequestController controller;
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    private final UserDto requesterDto = new UserDto(1L, "hgf", "kij@er.ty");
    private final ItemRequestDto requestDto = new ItemRequestDto(1L, "desc", requesterDto,
            LocalDateTime.of(2023, 8, 8, 0, 0, 0));

    @Test
    public void createRequestTest() {
        when(client.create(requestDto, requesterDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.create(requestDto, requesterDto.getId());
        assertEquals(response, objectResponseEntity);
    }

    @Test
    public void getItemRequestByIdTest() {
        when(client.getItemRequestById(requesterDto.getId(), requestDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.getItemRequestById(requesterDto.getId(), requestDto.getId());
        assertEquals(objectResponseEntity, response);
    }

    @Test
    public void getOwnItemRequestsTest() {
        when(client.getOwnItemRequests(requesterDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.getOwnItemRequests(requesterDto.getId());
        assertEquals(objectResponseEntity, response);
    }

    @Test
    public void getAllItemRequestsTest() {
        when(client.getAllItemRequests(requesterDto.getId(), 0, 10)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.getAllItemRequests(requesterDto.getId(), 0, 10);
        assertEquals(objectResponseEntity, response);
    }

}
