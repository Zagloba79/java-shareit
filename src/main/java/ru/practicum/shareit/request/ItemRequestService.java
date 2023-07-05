package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;


import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Long requesterId);

    ItemRequestDto getRequestById(Long id);

    ItemRequestDto update(ItemRequestDto itemRequestDto, Long itemId, Long requesterId);

    void delete(Long itemId, Long requesterId);

    List<ItemRequestDto> getItemsByRequester(Long requesterId);
}