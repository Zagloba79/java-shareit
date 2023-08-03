package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;


import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, Long requesterId);

    RequestWithItemsDto getRequestById(Long id, Long requesterId);

    List<RequestWithItemsDto> getRequestsByRequester(Long userId);

    List<RequestWithItemsDto> getRequestsPageable(Long userId, Integer from, Integer size);
}