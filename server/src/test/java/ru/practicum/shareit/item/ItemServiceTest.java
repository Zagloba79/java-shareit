package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemServiceImpl service;
    @Mock
    private ItemRepository repository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private EntityHandler handler;
    final User user = new User("user", "user@user.ru");
    final User requester = new User("requester", "requester@user.ru");
    final ItemRequest request = new ItemRequest(222L, "desc", requester, LocalDateTime.now());
    final ItemDto itemWithReqDto = new ItemDto("itemDto", "itemDtoDescription",
            true, 222L);
    final ItemDto itemWithoutReqDto = new ItemDto("itemDto", "itemDtoDescription",
            true, null);
    final Item itemWithReq = new Item("itemDto", "itemDtoDescription",
            true, user, request);
    final Item itemWithoutReq = new Item("itemDto", "itemDtoDescription",
            true, user, null);


    @Test
    @DirtiesContext
    public void createAndUpdateItemWithoutRequestTest() {
        user.setId(1L);
        doNothing().when(handler).itemValidate(any());
        when(handler.getUserFromOpt(user.getId())).thenReturn(user);
        when(repository.save(Mockito.any(Item.class))).thenReturn(itemWithoutReq);
        ItemDto itemFromDb = service.create(itemWithoutReqDto, user.getId());
        assertEquals("itemDto", itemFromDb.getName());
        assertEquals("itemDtoDescription", itemFromDb.getDescription());
        ItemDto newItemDto = new ItemDto("new", null,
                true, null);
        when(handler.getItemFromOpt(itemFromDb.getId())).thenReturn(ItemMapper.createItem(itemFromDb, user));
        ItemDto updatedItemDto = service.updateItem(newItemDto, itemFromDb.getId(), user.getId());
        assertEquals("new", updatedItemDto.getName());
        assertEquals("itemDtoDescription", updatedItemDto.getDescription());
        newItemDto = new ItemDto(null, "notNull",
                true, null);
        updatedItemDto = service.updateItem(newItemDto, itemFromDb.getId(), user.getId());
        assertEquals("new", updatedItemDto.getName());
        assertEquals("notNull", updatedItemDto.getDescription());
    }

    @Test
    @DirtiesContext
    public void createItemWithRequestTest() {
        user.setId(1L);
        doNothing().when(handler).itemValidate(any());
        when(handler.getUserFromOpt(user.getId())).thenReturn(user);
        when(repository.save(Mockito.any(Item.class))).thenReturn(itemWithReq);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        ItemDto itemFromDb = service.create(itemWithReqDto, user.getId());
        assertEquals("itemDto", itemFromDb.getName());
        assertEquals("itemDtoDescription", itemFromDb.getDescription());
        assertEquals(request.getId(), itemFromDb.getRequestId());

    }

    @Test
    @DirtiesContext
    void exceptionWhenGetItemWithWrongId() {
        user.setId(1L);
        long itemId = 234L;
        when(handler.getUserFromOpt(user.getId())).thenReturn(user);
        when(handler.getItemFromOpt(itemId)).thenThrow(new ObjectNotFoundException(
                "Предмета с " + itemId + " не существует."));
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> service.getItemById(itemId, user.getId()));
        assertEquals("Предмета с " + itemId + " не существует.", exception.getMessage());
    }
}