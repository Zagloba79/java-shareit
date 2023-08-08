package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    ItemClient itemClient;

    @InjectMocks
    ItemController itemController;

    private final Long userId = 1L;

    private final Long itemId = 1L;

    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @Test
    public void createItemTest() {
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 2L);

        when(itemClient.create(userId, itemDto)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> item = itemController.create(userId, itemDto);

        assertEquals(objectResponseEntity, item);
    }

    @Test
    public void findItem() {

        when(itemClient.getItemById(userId, itemId)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> item = itemController.getItemById(userId, itemId);

        assertEquals(objectResponseEntity, item);
    }

    @Test
    public void updateItemTest() {
        ItemDto itemDto = new ItemDto(23L, "man", "ds", true, 32L);

        when(itemClient.update(itemDto, itemId, userId)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> objectResponseEntity1 = itemController.update(itemDto, itemId, userId);

        assertEquals(objectResponseEntity1, objectResponseEntity);
    }

    @Test
    public void findAllItemByUserTest() {

        int from = 0;
        int size = 10;

        when(itemClient.getItemsByOwner(userId, from, size)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> allItemByUser = itemController.getItemsByOwner(userId, from, size);

        assertEquals(allItemByUser, objectResponseEntity);
    }

    @Test
    public void getItemsByQueryTest() {
        String text = "asd";
        int from = 0;
        int size = 10;

        when(itemClient.getItemsByQuery(text, from, size)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> search = itemController.getItemsByQuery(text, from, size);

        assertEquals(search, objectResponseEntity);
    }

    @Test
    public void createCommentTest() {
        CommentDto commentDto = new CommentDto(6L, "text", "ivan",
                LocalDateTime.of(2023, 1, 2, 3, 4, 5));

        when(itemClient.createComment(commentDto, itemId, userId)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> comment = itemController.createComment(commentDto, itemId, userId);

        assertEquals(comment, objectResponseEntity);
    }
}