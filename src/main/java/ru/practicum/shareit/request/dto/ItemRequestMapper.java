package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

public final class ItemRequestMapper {
    private ItemRequestMapper() {
    }

    public static ItemRequestDto createItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester(),
                itemRequest.getCreated());
    }

    public static ItemRequest createItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getRequester(),
                itemRequestDto.getCreated());
    }
}