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
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Long requesterId) {
        User requester = userStorage.getUser(requesterId);
        ItemRequest request = ItemRequestMapper.createItemRequest(itemRequestDto);
        itemRequestStorage.addRequest(request);
        ItemRequest requestFromStorage = itemRequestStorage.getRequest(request.getId());
        return ItemRequestMapper.createItemRequestDto(requestFromStorage);
    }

    @Override
    public ItemRequestDto getRequestById(Long itemRequestId) {
        ItemRequest itemRequest = itemRequestStorage.getRequest(itemRequestId);
        return ItemRequestMapper.createItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto update(ItemRequestDto itemRequestDto, Long itemId, Long requesterId) {
        Item item = itemStorage.getItem(itemId);
        User requester = userStorage.getUser(requesterId);
        Long itemRequestId = itemRequestDto.getId();
        ItemRequest itemRequest = itemRequestStorage.getRequest(itemRequestId);
        if (itemRequest.getRequester().equals(requester)) {
            ItemRequest request = ItemRequestMapper.createItemRequest(itemRequestDto);
            itemRequestStorage.update(request);
        }
        ItemRequest itemRequestFromStorage = itemRequestStorage.getRequest(itemRequestId);
        return ItemRequestMapper.createItemRequestDto(itemRequestFromStorage);
    }

    @Override
    public void delete(Long itemRequestId, Long requesterId) {
        User requester = userStorage.getUser(requesterId);
        ItemRequest itemRequest = itemRequestStorage.getRequest(itemRequestId);
        if (itemRequest.getRequester().equals(requester)) {
            itemRequestStorage.delete(itemRequest);
        }
    }

    @Override
    public List<ItemRequestDto> getItemsByRequester(Long requesterId) {
        return itemRequestStorage.getItemRequestsByRequester(requesterId).stream()
                .map(ItemRequestMapper::createItemRequestDto)
                .collect(toList());
    }
}