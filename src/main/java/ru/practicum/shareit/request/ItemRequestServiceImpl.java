package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;


import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Integer requesterId) {
        User requester = userStorage.getUser(requesterId);
        ItemRequest request = ItemRequestMapper.createItemRequest(itemRequestDto);
        itemRequestStorage.addRequest(request);
        ItemRequest requestFromStorage = itemRequestStorage.getRequest(request.getId());
        return ItemRequestMapper.createItemRequestDto(requestFromStorage);
    }

    @Override
    public ItemRequestDto getRequestById(Integer itemRequestId) {
        ItemRequest itemRequest = itemRequestStorage.getRequest(itemRequestId);
        return ItemRequestMapper.createItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto update(ItemRequestDto itemRequestDto, Integer itemId, Integer requesterId) {
        Item item = itemStorage.getItem(itemId);
        User requester = userStorage.getUser(requesterId);
        Integer itemRequestId = itemRequestDto.getId();
        ItemRequest itemRequest = itemRequestStorage.getRequest(itemRequestId);
        if (itemRequest.getRequester().equals(requester)) {
            ItemRequest request = ItemRequestMapper.createItemRequest(itemRequestDto);
            itemRequestStorage.update(request);
        }
        ItemRequest itemRequestFromStorage = itemRequestStorage.getRequest(itemRequestId);
        return ItemRequestMapper.createItemRequestDto(itemRequestFromStorage);
    }

    @Override
    public void delete(Integer itemRequestId, Integer requesterId) {
        User requester = userStorage.getUser(requesterId);
        ItemRequest itemRequest = itemRequestStorage.getRequest(itemRequestId);
        if (itemRequest.getRequester().equals(requester)) {
            itemRequestStorage.delete(itemRequest);
        }
    }

    @Override
    public List<ItemRequestDto> getItemsByRequester(Integer requesterId) {
        return itemRequestStorage.getItemRequestsByRequester(requesterId).stream()
                .map(ItemRequestMapper::createItemRequestDto)
                .collect(toList());
    }
}