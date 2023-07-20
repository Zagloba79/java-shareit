package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {
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

    public static ItemRequestWithAnswersDto createItemRequestWithAnswersDto(
            ItemRequest itemRequest, List<ItemForAnswerDto> items) {
        ItemRequestWithAnswersDto requestWithAnswers = new ItemRequestWithAnswersDto();
        requestWithAnswers.setDescription(itemRequest.getDescription());
        requestWithAnswers.setCreated(itemRequest.getCreated());
        requestWithAnswers.setAnswers(items);
        return requestWithAnswers;
    }
}