package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

public final class ItemRequestMapper {
    private ItemRequestMapper() {
    }

    public static ItemRequestDto createItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequester(itemRequest.getRequester());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public static ItemRequest createItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(itemRequestDto.getRequester());
        itemRequest.setCreated(itemRequestDto.getCreated());
        return itemRequest;
    }
}