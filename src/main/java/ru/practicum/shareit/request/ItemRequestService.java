package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;


import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Long requesterId);

    ItemRequestWithAnswersDto getRequestById(Long id, Long requesterId);

    ItemRequestDto update(ItemRequestDto itemRequestDto, Long itemId, Long requesterId);

    void delete(Long itemId, Long requesterId);

    List<ItemRequestWithAnswersDto> getRequestsByRequester(Long userId);

    List<ItemRequestDto> getRequestsInQuantity(Long userId, Integer from, Integer size);
}