package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingForDataDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ItemMapperTest {

    @Test
    public void createItemDtoWithoutRequestTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        Item item = new Item("name", "desc", true, owner, null);
        item.setId(123L);
        ItemDto itemDto = ItemMapper.createItemDto(item);
        assertEquals(123L, itemDto.getId());
        assertEquals("name", itemDto.getName());
        assertEquals("desc", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertNull(itemDto.getRequestId());
    }

    @Test
    public void createItemDtoWithRequestTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        User requester = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        requester.setId(5L);
        ItemRequest request = new ItemRequest("reqDesc", requester, LocalDateTime.now());
        Item item = new Item("name", "desc", true, owner, request);
        item.setId(125L);
        ItemDto itemDto = ItemMapper.createItemDto(item);
        assertEquals(125L, itemDto.getId());
        assertEquals("name", itemDto.getName());
        assertEquals("desc", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertEquals(request.getId(), itemDto.getRequestId());
    }

    @Test
    public void createItemWithCommentsAndBookingsDtoTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        User booker = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        booker.setId(15L);
        Item item = new Item("name", "desc", true, owner, null);
        item.setId(130L);
        BookingForDataDto lastBooking = new BookingForDataDto(100L, booker.getId(),
                LocalDateTime.of(2013, 1, 1, 1, 2, 3),
                LocalDateTime.of(2013, 1, 1, 1, 2, 4));
        BookingForDataDto nextBooking = new BookingForDataDto(101L, booker.getId(),
                LocalDateTime.of(2023, 11, 1, 1, 2, 3),
                LocalDateTime.of(2023, 11, 1, 1, 2, 4));
        CommentDto comment = new CommentDto("tekst", booker.getName(),
                LocalDateTime.of(2023, 2, 1, 1, 2, 3));
        ItemWithCommentsAndBookingsDto itemDto = ItemMapper.createItemWithCommentsAndBookingsDto(item);
        assertEquals(130L, itemDto.getId());
        assertEquals("name", itemDto.getName());
        assertEquals("desc", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());
        assertNull(itemDto.getComments());
        assertNull(itemDto.getLastBooking());
        assertNull(itemDto.getNextBooking());
        itemDto.setLastBooking(lastBooking);
        assertEquals(lastBooking, itemDto.getLastBooking());
        assertEquals(100L, itemDto.getLastBooking().getId());
        assertEquals(15L, itemDto.getLastBooking().getBookerId());
        itemDto.setComments(List.of(comment));
        assertEquals(comment, itemDto.getComments().get(0));
        itemDto.setNextBooking(nextBooking);
        assertEquals(nextBooking, itemDto.getNextBooking());
        assertEquals(101L, itemDto.getNextBooking().getId());
        assertEquals(15L, itemDto.getNextBooking().getBookerId());
    }

    @Test
    public void createItemForAnswerDtoTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        User requester = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        requester.setId(5L);
        ItemRequest request = new ItemRequest("reqDesc", requester, LocalDateTime.now());
        Item item = new Item("name", "desc", true, owner, request);
        item.setId(125L);
        ItemForAnswerDto itemForAnswerDto = ItemMapper.createItemForAnswerDto(item, requester.getId());
        assertEquals(125L, itemForAnswerDto.getId());
        assertEquals("name", itemForAnswerDto.getName());
        assertEquals("desc", itemForAnswerDto.getDescription());
        assertEquals(true, itemForAnswerDto.getAvailable());
    }

    @Test
    public void createItemTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        ItemDto itemDto = new ItemDto("name", "descDesc", false, null);
        Item item = ItemMapper.createItem(itemDto, owner);
        assertEquals("name", item.getName());
        assertEquals("descDesc", item.getDescription());
        assertEquals(false, item.getAvailable());
        assertEquals(owner, item.getOwner());
        assertNull(item.getRequest());
    }
}
