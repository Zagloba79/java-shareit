package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
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

    public static ItemRequest createNewItemRequest(ItemRequestDto itemRequestDto, User requester) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static RequestWithItemsDto createItemRequestWithAnswersDto(
            ItemRequest itemRequest, List<ItemForAnswerDto> itemsForAnswer) {
        RequestWithItemsDto requestWithAnswers = new RequestWithItemsDto();
        requestWithAnswers.setId(itemRequest.getId());
        requestWithAnswers.setDescription(itemRequest.getDescription());
        requestWithAnswers.setCreated(itemRequest.getCreated());
        requestWithAnswers.setItems(itemsForAnswer);
        return requestWithAnswers;
    }
}