package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemServiceImpl service;
    @Mock
    private ItemRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EntityHandler handler;
    User user = new User("user", "user@user.ru");
    UserDto userDto = new UserDto("user", "user@user.ru");
    ItemDto itemDto = new ItemDto("itemDto", "itemDtoDescription",
            true, null);
    Item item = new Item("itemDto", "itemDtoDescription", true, user, null);

    @Test
    public void createAndUpdateItemTest() {
        user.setId(1L);
        doNothing().when(handler).itemValidate(any());
        when(handler.getUserFromOpt(user.getId())).thenReturn(user);
        when(repository.save(Mockito.any(Item.class))).thenReturn(item);
        ItemDto itemFromDb = service.create(itemDto, user.getId());
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

//    @Test
//    void exceptionWhenGetItemWithWrongId() {
//        user.setId(1L);
//        long itemId = 234L;
//        when(handler.getUserFromOpt(user.getId())).thenReturn(user);
//        when(repository.findById(itemId)).thenReturn(Optional.empty());
//        when(handler.getItemFromOpt(itemId)).thenThrow(ObjectNotFoundException.class);
////        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
////                () -> handler.getItemFromOpt(itemId));
//        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
//                () -> service.getItemById(itemId, user.getId()));
//        assertEquals("Предмета с " + itemId + " не существует.", exception.getMessage());
//
//    }
}