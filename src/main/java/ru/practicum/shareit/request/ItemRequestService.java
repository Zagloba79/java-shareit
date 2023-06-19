package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, int requesterId);

    ItemRequestDto getRequestById(int id);

    ItemRequestDto update(ItemRequestDto itemRequestDto, int itemId, int requesterId);

    List<ItemRequestDto> getItemRequestsByItem(Item item);

    void delete(int itemId, int requesterId);

    List<ItemRequestDto> getItemsByRequester(int requesterId);
}