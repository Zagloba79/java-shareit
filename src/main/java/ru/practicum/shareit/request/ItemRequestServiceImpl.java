package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
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
public class ItemRequestServiceImpl implements ItemRequestService {

    private final InMemoryItemRequestStorage itemRequestStorage;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private Integer itemRequestId = 1;

    @Autowired
    public ItemRequestServiceImpl(InMemoryItemRequestStorage itemRequestStorage,
                                  ItemRequestMapper itemRequestMapper,
                                  ItemStorage itemStorage, UserStorage userStorage) {
        this.itemRequestStorage = itemRequestStorage;
        this.itemRequestMapper = itemRequestMapper;
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, Integer requesterId) {
        User requester = userStorage.getUser(requesterId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + requesterId + " не существует."));
        ItemRequest request = new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                requester,
                itemRequestDto.getCreated());
        request.setId(itemRequestId++);
        itemRequestStorage.addRequest(request);
        ItemRequest itemRequest = itemRequestStorage.getRequestById(request.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + request.getId() + " не существует."));
        return itemRequestMapper.createItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getRequestById(Integer id) {
        ItemRequest itemRequest = itemRequestStorage.getRequestById(id).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + id + " не существует."));
        return itemRequestMapper.createItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto update(ItemRequestDto itemRequestDto, Integer itemId, Integer requesterId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        User requester = userStorage.getUser(requesterId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + requesterId + " не существует."));
        Integer itemRequestId = itemRequestDto.getId();
        ItemRequest itemRequest = itemRequestStorage.getRequestById(itemRequestId).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + itemRequestId + " не существует."));
        if (itemRequest.getRequester().equals(requester)) {
            ItemRequest request = itemRequestMapper.createItemRequest(itemRequestDto);
            itemRequestStorage.update(request);
        }
        ItemRequest itemRequestFromStorage = itemRequestStorage.getRequestById(itemRequestId).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + itemRequestId + " не существует."));
        return itemRequestMapper.createItemRequestDto(itemRequestFromStorage);
    }

    @Override
    public void delete(Integer itemRequestId, Integer requesterId) {
        User requester = userStorage.getUser(requesterId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + requesterId + " не существует."));
        ItemRequest itemRequest = itemRequestStorage.getRequestById(itemRequestId).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + itemRequestId + " не существует."));
        if (itemRequest.getRequester().equals(requester)) {
            itemRequestStorage.delete(itemRequest);
        }
    }

    @Override
    public List<ItemRequestDto> getItemsByRequester(Integer requesterId) {
        return itemRequestStorage.getItemRequestsByRequester(requesterId).stream()
                .map(itemRequestMapper::createItemRequestDto)
                .collect(toList());
    }
}