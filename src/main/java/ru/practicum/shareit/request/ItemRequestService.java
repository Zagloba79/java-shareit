package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;


import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Integer requesterId);

    ItemRequestDto getRequestById(Integer id);

    ItemRequestDto update(ItemRequestDto itemRequestDto, Integer itemId, Integer requesterId);

    //List<ItemRequestDto> getItemRequestsByItem(Integer itemId);

    void delete(Integer itemId, Integer requesterId);

    List<ItemRequestDto> getItemsByRequester(Integer requesterId);
}