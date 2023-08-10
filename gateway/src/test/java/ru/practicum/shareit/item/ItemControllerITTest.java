package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerITTest {
    @Mock
    private ItemClient client;
    @InjectMocks
    private ItemController controller;
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    private final UserDto userDto = new UserDto(1L, "hgf", "kij@er.ty");
    private final ItemDto itemDto = new ItemDto(1L, "name", "desc", true, null);

    @Test
    public void createItemTest() {
        when(client.create(userDto.getId(), itemDto)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.create(userDto.getId(), itemDto);
        assertEquals(response, objectResponseEntity);
    }

    @Test
    public void getItemsByOwnerTest() {
        when(client.getItemsByOwner(userDto.getId(), 0, 10)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.getItemsByOwner(userDto.getId(), 0, 10);
        assertEquals(response, objectResponseEntity);
    }

    @Test
    public void getItemByIdTest() {
        when(client.getItemById(userDto.getId(), itemDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.getItemById(userDto.getId(), itemDto.getId());
        assertEquals(response, objectResponseEntity);
    }

    @Test
    public void updateItemTest() {
        ItemDto newItemDto = new ItemDto(1L, "newName", "newDesc", true, null);
        when(client.update(newItemDto, itemDto.getId(), userDto.getId())).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.update(itemDto.getId(), newItemDto, userDto.getId());
        assertEquals(objectResponseEntity, response);
    }

    public void deleteItemTest() {
        controller.delete(itemDto.getId(), userDto.getId());
        Mockito.verify(client, Mockito.times(1)).delete(itemDto.getId(), userDto.getId());
    }

    @Test
    public void getItemsByQueryTest() {
        when(client.getItemsByQuery(userDto.getId(), "text", 0, 10)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.getItemsByQuery(userDto.getId(),"text",0, 10);
        assertEquals(objectResponseEntity, response);
    }

    @Test
    public void createCommentTest() {
        when(client.getItemsByQuery(userDto.getId(), "text", 0, 10)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> response = controller.getItemsByQuery(userDto.getId(),"text",0, 10);
        assertEquals(objectResponseEntity, response);
    }
}
