package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;


import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Integer requesterId) {
        User requester = userService.userFromStorage(requesterId);
        ItemRequest request = new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                requester,
                itemRequestDto.getCreated());
        itemRequestStorage.addRequest(request);
        ItemRequest itemRequest = itemRequestFromStorage(request.getId());
        return ItemRequestMapper.createItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getRequestById(Integer itemRequestId) {
        ItemRequest itemRequest = itemRequestFromStorage(itemRequestId);
        return ItemRequestMapper.createItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto update(ItemRequestDto itemRequestDto, Integer itemId, Integer requesterId) {
        Item item = itemService.itemFromStorage(itemId);
        User requester = userService.userFromStorage(requesterId);
        Integer itemRequestId = itemRequestDto.getId();
        ItemRequest itemRequest = itemRequestFromStorage(itemRequestId);
        if (itemRequest.getRequester().equals(requester)) {
            ItemRequest request = ItemRequestMapper.createItemRequest(itemRequestDto);
            itemRequestStorage.update(request);
        }
        ItemRequest itemRequestFromStorage = itemRequestFromStorage(itemRequestId);
        return ItemRequestMapper.createItemRequestDto(itemRequestFromStorage);
    }

    @Override
    public void delete(Integer itemRequestId, Integer requesterId) {
        User requester = userService.userFromStorage(requesterId);
        ItemRequest itemRequest = itemRequestFromStorage(itemRequestId);
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

    @Override
    public ItemRequest itemRequestFromStorage(Integer itemRequestId) {
        return itemRequestStorage.getRequestById(itemRequestId).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + itemRequestId + " не существует."));
    }
}