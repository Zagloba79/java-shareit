package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ItemRequestMapperTest {

    @Test
    public void createItemRequestDtoTest() {
        User user = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        ItemRequest itemRequest = new ItemRequest("reqDesc", user, LocalDateTime.of(
                2023, 11, 12, 0, 1, 2));
        itemRequest.setId(12L);
        ItemRequestDto itemRequestDto = ItemRequestMapper.createItemRequestDto(itemRequest);
        assertEquals(12L, itemRequestDto.getId());
        assertEquals("reqDesc", itemRequestDto.getDescription());
        assertEquals(user, itemRequestDto.getRequester());
    }

    @Test
    public void createNewItemRequestTest() {
        User user = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        ItemRequestDto itemRequestDto = new ItemRequestDto("reqDtoDesc", LocalDateTime.of(
                2023, 11, 12, 0, 1, 2));
        itemRequestDto.setId(1L);
        ItemRequest itemRequest = ItemRequestMapper.createNewItemRequest(itemRequestDto, user);
        assertEquals("reqDtoDesc", itemRequest.getDescription());
        assertEquals(user, itemRequest.getRequester());
    }

    @Test
    public void createItemRequestWithAnswersDtoTest() {
        User requester = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        User owner = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        ItemRequest itemRequest = new ItemRequest("reqDesc", requester, LocalDateTime.of(
                2023, 11, 12, 0, 1, 2));
        itemRequest.setId(21L);
        ItemForAnswerDto itemForAnswerDto = ItemMapper.createItemForAnswerDto(new Item("item1",
                "desc1", true, owner, itemRequest), itemRequest.getId());
        ItemForAnswerDto itemForAnswerSec = ItemMapper.createItemForAnswerDto(new Item("item2",
                "desc2", true, owner, itemRequest), itemRequest.getId());
        RequestWithItemsDto requestWithAnswersDto = ItemRequestMapper.createItemRequestWithAnswersDto(
                itemRequest, List.of(itemForAnswerDto, itemForAnswerSec));
        assertEquals("reqDesc", requestWithAnswersDto.getDescription());
        assertEquals(2, requestWithAnswersDto.getItems().size());
        assertEquals("item1", requestWithAnswersDto.getItems().get(0).getName());
        assertEquals("desc2", requestWithAnswersDto.getItems().get(1).getDescription());
    }
}
