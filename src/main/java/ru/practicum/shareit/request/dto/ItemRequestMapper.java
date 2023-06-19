package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
@AllArgsConstructor
public class ItemRequestMapper {
    public ItemRequestDto createItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getItem(),
                itemRequest.getRequester());
    }

    public ItemRequest createItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getItem(),
                itemRequestDto.getRequester());
    }
}